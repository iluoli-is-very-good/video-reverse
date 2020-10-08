package com.iluoli.reverse.plugin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

public class Mogujie extends BaseExecute{
	
	

	public Mogujie(String url) {
		super(url);
	}
	
	String mw_appkey = "100028";
	String mw_ttid = "NMMain@mgj_h5_1.0";
	String mw_t = String.valueOf(System.currentTimeMillis() / 1000);
	String mw_h5_os = "iOS";
	
	
	
	@Override
	public String execute() {
		long time1 = System.currentTimeMillis();
		url = getRealPlayAddress(url);
		
		String iid = UrlUtil.parse(url).params.get("iid");
		System.out.println("iid : " + iid);
		
		Map<String, String> tokenMap = getToken();
		
		String data1 = "{\"iid\":\"" + iid + "\",\"clientType\":\"h5\"}";
		String param = getParam(data1, tokenMap.get("__mgjuuid"));
		
		
		String cookie = "__mgjuuid=" + tokenMap.get("__mgjuuid") + "; _mwp_h5_token_enc=" + tokenMap.get("encToken")  + "; _mwp_h5_token=" + tokenMap.get("token");
		//3.2 加密data1获取sign值
		String sign = sign(data1);
		//3.2组装参数，再次加密,获取mw-sign值
		String befor = mw_appkey + "&" + mw_h5_os + "&" + mw_t + "&" + mw_ttid + "&"+ tokenMap.get("__mgjuuid") +"&mwp.darling.feedById&1&" + sign + "&" + tokenMap.get("token");
		String mw_sign = sign(befor);
		//3.3请求数据（取得videoId）
		param += "&mw-sign=" + mw_sign;
		
		HttpRequest get1 = HttpUtil.createGet("https://api.mogu.com/h5/mwp.darling.feedById/1/?" + param);
		get1.cookie(cookie);
		get1.header("referer", url);
		
		String videoId = JSONObject.parseObject(get1.execute().body()).getJSONObject("data").getJSONObject("data").getJSONObject("looks").getJSONObject("video").getString("videoId");
//		String pic = JSONObject.parseObject(get1.execute().body()).getJSONObject("data").getJSONObject("data").getJSONObject("looks").getJSONObject("video").getString("firstFrame");
//		String title = JSONObject.parseObject(get1.execute().body()).getJSONObject("data").getJSONObject("data").getString("content");
		
		//4.获取真实mp4（跟3很像  不过token之类的已经有了，只管数据加密就好）
		//4.1
		String data2 = "{\"videoId\":\"" + videoId + "\",\"clientType\":\"h5\"}";
		String param2 = getParam(data2 , tokenMap.get("__mgjuuid"));
		//4.2加密data1获取sign值
		String sign2 = sign(data2);
		
		//4.3组装参数，再次加密,获取mw-sign值
		String befor2 = mw_appkey + "&" + mw_h5_os + "&" + mw_t + "&" + mw_ttid + "&"+ tokenMap.get("__mgjuuid") +"&mwp.media.queryVideo&1&" + sign2 + "&" + tokenMap.get("token");
		String mw_sign2 = sign(befor2);
		
		//4.4请求数据
		param2 += "&mw-sign=" + mw_sign2;

		HttpRequest get2 = HttpUtil.createGet("https://api.mogu.com/h5/mwp.media.queryVideo/1/?" + param2);
		get2.cookie(cookie);
		get2.header("referer", url);
		long time2 = System.currentTimeMillis();
		System.out.println("耗时：" + (time2 - time1));
		return JSONObject.parseObject(get2.execute().body()).getJSONObject("data").getJSONArray("playSet").getJSONObject(0).getString("url");
	}
	
	public static String get(String address) {
		return new Mogujie(address).execute();
	}
	
	
	public interface JavaScriptInterface{
		public String z(String sign);
	}
	
	public Map<String , String> getToken(){
		
		// 1.获取uuid
		HttpResponse response = request("https://list.mogujie.com");
		String __mgjuuid = response.getCookieValue("__mgjuuid");

		// 2.从url中拿到iid
		
		// 3.获取videoId

		// 3.1组装参数（用来获取token值）
		String data1 = "{\"iid\":\"" + "1" + "\",\"clientType\":\"h5\"}";
		String param = getParam(data1, __mgjuuid);
		// 获取token
		HttpRequest get = HttpUtil.createGet("https://api.mogu.com/h5/mwp.darling.feedById/1/?" + param);

		get.header("referer", url);
		JSONObject body = JSONObject.parseObject(get.execute().body());
		String token = body.getString("token");
		String encToken = body.getString("encToken");
		
		HashMap<String, String> map = new HashMap<>();
		map.put("__mgjuuid", __mgjuuid);
		map.put("encToken", encToken);
		map.put("token", token);
		
		return map;
	}
	
	
	public String sign(String str) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js"); 
		try {
			InputStream stream = getClass().getClassLoader().getResourceAsStream("mogu_sign.js");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
//			String classesPath = Mogujie.class.getClassLoader().getResource("classpath:").getPath(); 
			engine.eval(br);
			
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		if (engine instanceof Invocable) { 
			Invocable invocable = (Invocable) engine; 
			JavaScriptInterface executeMethod = invocable.getInterface(JavaScriptInterface.class); 
			return executeMethod.z(str);
		}
		throw new RuntimeException("验签失败");
	}
	
	
	public String getParam(String data , String __mgjuuid) {
		return "data=" + URLEncoder.encode(data) + "&mw-appkey=" + mw_appkey + "&mw-ttid=" + URLEncoder.encode(mw_ttid) + "&mw-t=" + mw_t + "&mw-uuid=" + __mgjuuid + "&mw-h5-os=" + mw_h5_os;
	}
	
	public static void main(String[] args) {
//		System.out.println(Mogujie.get("https://g.mogu.com/192MGkEF"));
		
		HttpRequest get = HttpUtil.createGet("https://api.mogu.com/h5/mwp.darling.feedById/1/?data=%7B%22iid%22%3A%22127xjac%22%2C%22clientType%22%3A%22h5%22%7D&mw-appkey=100028&mw-ttid=NMMain%40mgj_h5_1.0&mw-t=1582085376974&mw-uuid=adb5ce39-cab1-49cc-bce0-2a13d0fcc864&mw-h5-os=iOS&mw-sign=ed2db1c83ce90d47b0204794348d6883&callback=mwpCb1&_=1582085383177");
//		get.cookie("__mgjuuid=adb5ce39-cab1-49cc-bce0-2a13d0fcc864; _mwp_h5_token_enc=888824af6023122095e8b444cb078bd2; _mwp_h5_token=b0caa2534094ec2cd074b877478622d5_1581999108269");
		get.header("referer", "https://h5.mogu.com/brand-content/content.html?iid=127xjac");
		System.out.println(get.execute().body());
	}
}
