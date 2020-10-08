package com.iluoli.reverse.plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YoukuVideo extends BaseExecute{

	String jsv = "2.5.8";
	String appKey = "24679788";
	String v = "1.1";
	String YKPid = "20160317PLF000211";
	
	public YoukuVideo(String url) {
		super(url);
	}

	@Override
	public String execute() {
		
		HttpRequest get = HttpUtil.createGet(url);
		String cookieStr = "__wpkreporterwid_=164df789-99e0-44b5-1b20-9427b0c127e2; __ysuid=15812310188755VW; cna=mrbBFguezxoCAd8VUkkVxM1y; UM_distinctid=174875eb811174-065dc204fbfe6a-f313f6d-1fa400-174875eb81236f; __aysid=1599998966811vW7; Hm_lvt_eaa57ca47dacb4ad4f5a257001a3457c=1600001348,1600005292,1600005322,1600007984; __aysvstp=41; _m_h5_tk=c6b6a671b6bed63776aa0a540f2921a9_1600146058399; _m_h5_tk_enc=d8261c20c03a142e68a06e27a85e0af2; __ayft=1600140840223; __ayscnt=1; __modalSkinColor=dark; P_ck_ctl=2CB6285DA2DD46FDE79A59E489B4DAC5; modalFrequency={\"UUID\":\"4\",\"times\":1,\"firstTimeExpire\":1600227241775}; __arpvid=16001408504685fv1XC-1600140850540; __arycid=dc-3-00; __arcms=dc-3-00; __aypstp=2; __ayspstp=19; isg=BGpqyskxwUUm_Uy5VHSJhCAiu9AM2-41tt1s6fQjdr1GJwvh3G4vR7Zfs1M712bN";
		get.cookie(cookieStr);
		String[] split3 = cookieStr.split("; ");
		Map<String, String> cookieMap = new HashMap<String, String>();
		if(cookieStr != "" && cookieStr != null) {
			for(String str : split3) {
				cookieMap.put(str.split("=")[0], str.split("=")[1]);
			}
		}
		
		String t = "1600140860473";
		get.form("jsv", jsv);
		get.form("appKey", appKey);
		get.form("t", t);
		
		get.form("api", "mtop.youku.play.ups.appinfo.get");
		get.form("v", v);
		get.form("timeout", "20000");
		get.form("YKPid", YKPid);
		get.form("YKLoginRequest", true);
		get.form("AntiFlood", true);
		get.form("AntiCreep", true);
		get.form("type", "jsonp");
		get.form("dataType", "jsonp");
		get.form("callback", "mtopjsonp1");
		Map<String , String> data = new LinkedHashMap<>();
		Map<String, Object> steal_params = new LinkedHashMap<>();
		steal_params.put("ccode", "0502");
		steal_params.put("client_ip", "192.168.1.1");
		steal_params.put("utid", cookieMap.get("cna"));
		steal_params.put("client_ts", "1600008032");
		steal_params.put("version", "2.1.27");
		steal_params.put("ckey", "134#HvXwaJXwXGfYDrwUJgN9dX0D3QROwKO9sE/w4/PLr3cuDvoeulSN91ticga0Gjyj43db5acQ0b6Ww7S2LClmD9U2alydOM0bYLpQzAls6khE9gowqqX3IkuU+4qAqVtu12nxzsefqc77ogjzuqp1ASUvXX2Kqc11TvtE+TpKqH75o2N4yKr9PJoa7Q58/oUjz/Ebslo/rR+o0DI625bpM1nv2avablSZTc+tIjSRlYUStBnVd4l2Ovt1bkVtjstZbN0lPivQiFxlm21EeF4q9MdHN6ny0Jc6airwtYNft21oKIqrxljNXwLmv/ldP10uFhM293eVrsEkjfM3bBMNCxBzFmMxbIbob5ZZFT/XIAtT/ujVl2vQxYwmu3oywq2+XdS6PJYfZdRaqUOUKVYY2/1gvTYynkbffU5AmfAdBTkeik9HnL8kbdRfqkkMWy/TjaAk85F62sPS/7B9Rb057XidlyBhlHWzJpfOvcZKOS4AKKx4SHVuxwbJU6L1imfr7oUAgeOc/0S+RW3bCsQL6o870clh9TvHySJ+n2SKMaaohITfNBqttSHrVNwcM/K8bSA2WfJYIBckpqi+INWQVVAyRFRItjq0qizvSmvtgqvXkDPdJM2ywM35ox+mT0UjZlosBV9KO7mpXwQwq0vFX+NgdGLIgiKHhyCcJxj577o8CGunPB4LSnrIo/dYkyseftN6/hkcrjQmShAk2MZYqvUvMgYKuHLm1vz+e6vl0HcaLbtu9cFqUfK161JzDVlrcPXXPpa6q171SEZerveWI0yeAMj1vEjmrYsdZP+n39o/2xQWzoDTo7Z78Tbugy9ob26TiaS39iwXP5nejXMyKeIbSPKfJrDaxtPFLE3Ri4jOEvxZDY8xiordJBLnTnUbdroYLZSkhm8PTutSMCLn21t6l+SCwFRfu3bmyCuHvaoV41ATMuVtCuaGRFEUSwnEYQ/aZIpnLvUvB4zDxfcizlONmy0/jL4vqL8qx2L9NTJHaFCeVrL96lzaQV8vFJUzmbcs2+Ibaw0yujdP/A1yhE3dLwSFLzEO8d47tlxGPJWBxtvgXTs8omdZf3h39uar5OIgZyyhjpjwlmseVrfa+68hxoIQNBGjLa79e3Hh3m8iAb1SCRt8s1s9I8e7aom3lhRM+IjyKoY3ntx9tMOenKOM9KSQqIbPVeixezqbwTCjSRGh3i0i3cHQgFEtrOEX7eV6XggB4nczhliBjCCBDkeLiGvwkm5CQlXbTBHo3M4VeEkqtlFBthK3BCTc5ayivh/IqOmDOjJvRDOPFI/RyDGeLrlVWhJoTsGsOxSOthRDPVcmKhallMOrn807kslzXRangwAg76nCO8lKL2FxuwW1Csgk1y+Ku6hD7vX5PsCpyO63rtElGCdinKMzmXF2b/VS3KKxktsmLs8k+4HOZBsJj0Gm6pHIP7QKfhVuVQhl/RAfmqhX4He+zjEcSYMTA840WI3yevI3lBVqxxhlJK/WHmpNE511EAEC5xVMfA+sfW8WIRekqZ5WKPpFqPcjT4l+st3FzFVFgLG8KjbAFvGS+XfPhaX9qy7kiPKEsX9iFik385CbBOKRs74iomKdZPAhWgw=");
		//如果参数ckey有误，会返回无权播放  
		//{"api":"mtop.youku.play.ups.appinfo.get","data":{"cost":0.003,"data":{"ups":{"ups_ts":"1600142033","ups_client_netip":"127.0.0.1","psid":"c9f0a0262be82162faf6089fb5cbc5114109c"},"error":{"note":"客户端无权播放,201","code":-6004,"ok":false},"remoteDebug":[]},"e":{"code":0,"provider":"hsfprovider","desc":""}},"ret":["SUCCESS::调用成功"],"v":"1.1"}
		Map<String, Object> biz_params = new LinkedHashMap<>();
		biz_params.put("vid", "XNDc4NTg1NzE1Mg==");
		biz_params.put("play_ability", 5376);
		biz_params.put("current_showid", "406595");
		biz_params.put("drm_type", 19);
		biz_params.put("key_index", "web01");
		biz_params.put("encryptR_client", "yyDwwdr7EdGu4OafX3L5rg==");
		biz_params.put("preferClarity", 99);
		biz_params.put("extag", "EXT-X-PRIVINF");
		biz_params.put("master_m3u8", 1);
		biz_params.put("media_type", "standard,subtitle");
		biz_params.put("app_ver", "2.1.27");
		
		Map<String, Object> ad_params = new LinkedHashMap<>();
		ad_params.put("vs", "1.0");
		ad_params.put("pver", "2.1.27");
		ad_params.put("sver", "2.0");
		ad_params.put("site", 1);
		ad_params.put("aw", "w");
		ad_params.put("fu", 0);
		ad_params.put("d", "0");
		ad_params.put("bt", "pc");
		ad_params.put("os", "win");
		ad_params.put("osv", "10");
		ad_params.put("dq", "auto");
		ad_params.put("atm", "");
		ad_params.put("partnerid", "null");
		ad_params.put("wintype", "interior");
		ad_params.put("isvert", 0);
		ad_params.put("vip", 0);
		ad_params.put("emb", "AjExOTY0NjQyODgCdi55b3VrdS5jb20CL3Zfc2hvdy9pZF9YTkRjNE5UZzFOekUxTWc9PS5odG1s");
		ad_params.put("p", 1);
		ad_params.put("rst", "mp4");
		ad_params.put("needbf", 2);
		ad_params.put("avs", "1.0");
		data.put("steal_params", JSONObject.toJSONString(steal_params));
		data.put("biz_params", JSONObject.toJSONString(biz_params));
		data.put("ad_params", JSONObject.toJSONString(ad_params));
//		String data = "{\"steal_params\":\"{\\\"ccode\\\":\\\"0502\\\",\\\"client_ip\\\":\\\"192.168.1.1\\\",\\\"utid\\\":\\\"mrbBFguezxoCAd8VUkkVxM1y\\\",\\\"client_ts\\\":1600005309,\\\"version\\\":\\\"2.1.27\\\",\\\"ckey\\\":\\\"134#Ru64G6XwXGMTa7dgTXh4dX0D3QROwKO9sE/w4/PLr3cuDvoeuatTtQQTBo78zX/eg5XMQ/rxMg4dQZZys6211YIXl7bQi6/y9s7oagowqqX3IkuU+4qAqVtu12nxzsefqc77oXowqcp1qkgB+X2KqcqqZIeBnR4Py6wBiO51/oUjz/EYqRpivCqMR6qRoOYY1onscjwj5jerUX5DUeMXWqdCfJUw1s0KsG0TIlfF6IWGKmCQQHlWrpFeKXOjvNZ4e6YudHWG3fIrjXJMUlOxsAyHBuG5BGl+VY4GpcATo+oMsWwD/jXdXPiy4Cw24qTmkKv2snneEWZEoc0dz9Gj4ByURzXN9+KuRFFaoQpk/RZ+3/bv6yPkZfQLFxzdx5iFPBnUoczNcmBH4FYFRKzL0T0fjwWucAc/74QqmXfFMNsIv3ISlgkKfXfFX6pgNiXXA1aAhvJjKenbDhP+mScTfZeMjCfWWBtZclEgDash+zd09Ou1W4NBksGQg+Ovtg3tbwVaoQrnpGcejry6yazKwylwwuFFRsNw45nKVHq96buQ0/P0v+bIn3/fshg0a1rOWp0Jm3a88yjqWWZcyqkLoNq+jBPC59DrtQErc+gSdDd9OuoGL6rhGxUIvWm2aMSIOdgkCFnKVDStP6sx8gAZq7enNeZo8x/AhETOAyh7pw/5fCloiUuRJJYFECbay95Dqt5UGLIAwQui7zWylUzfyQrIxt3u5c+IyPB42TXoD8Le1K84XtLjtG//znO9EFe8auKzdN8GBDIIdJJ3ERbg8qTv3BGhVyTHzg9QmVsjTntKlG5VU+ejC6G1LjTDruAp5i06/k6Nv8gwd2jd7C17ibQN4GCqBz4yPxkTzMbEygqFUK1Fk1WP/dq9a8tVcva4O8z6A2Fzue9cPhq6oHcaYu+DVR5Fef/97S0KwPoumnaVm2U7fLJ3Esa28bU7TwA18oWV9X9hmOseQcgRqUiYn2pRWRZ/+TZYuvwbAZKngwEkKu2I5RVVxmHbZF2KUa03nJmVVo5BJQgBM+TTc4OwaT7f7pJdViqrSM+p8glXQn2/dWNUoUNa5UuIXfXPZbX=\\\"}\",\"biz_params\":\"{\\\"vid\\\":\\\"XNDc4NTg1NzE1Mg==\\\",\\\"play_ability\\\":5376,\\\"current_showid\\\":\\\"406595\\\",\\\"drm_type\\\":19,\\\"key_index\\\":\\\"web01\\\",\\\"encryptR_client\\\":\\\"2llrzREJDaAUiBBSDOWzkA==\\\",\\\"preferClarity\\\":99,\\\"extag\\\":\\\"EXT-X-PRIVINF\\\",\\\"master_m3u8\\\":1,\\\"media_type\\\":\\\"standard,subtitle\\\",\\\"app_ver\\\":\\\"2.1.27\\\"}\",\"ad_params\":\"{\\\"vs\\\":\\\"1.0\\\",\\\"pver\\\":\\\"2.1.27\\\",\\\"sver\\\":\\\"2.0\\\",\\\"site\\\":1,\\\"aw\\\":\\\"w\\\",\\\"fu\\\":0,\\\"d\\\":\\\"0\\\",\\\"bt\\\":\\\"pc\\\",\\\"os\\\":\\\"win\\\",\\\"osv\\\":\\\"10\\\",\\\"dq\\\":\\\"auto\\\",\\\"atm\\\":\\\"\\\",\\\"partnerid\\\":\\\"null\\\",\\\"wintype\\\":\\\"interior\\\",\\\"isvert\\\":0,\\\"vip\\\":0,\\\"emb\\\":\\\"AjExOTY0NjQyODgCbW92aWUueW91a3UuY29tAi8=\\\",\\\"p\\\":1,\\\"rst\\\":\\\"mp4\\\",\\\"needbf\\\":2,\\\"avs\\\":\\\"1.0\\\"}\"}";
		get.form("data", JSONObject.toJSONString(data));
		
		get.form("sign", getSign(cookieMap.get("_m_h5_tk").split("_")[0], t, "24679788", JSONObject.toJSONString(data)));
		
		
		
		
		HttpResponse response = get.execute();
		String body = response.body().replaceAll(" mtopjsonp1\\(", "").replaceAll("\\)", "");
		JSONObject json = JSONObject.parseObject(body);
		System.out.println(body);
		JSONArray streams = json.getJSONObject("data").getJSONObject("data").getJSONArray("stream");
		for (int i = 0; i < streams.size(); i++) {
			JSONObject stream = streams.getJSONObject(i);
			System.out.println(stream.getString("size"));
			System.out.println(stream.getString("m3u8_url"));
		}
		
//		System.out.println(sign(""));
//		System.out.println(json.getJSONObject("data").getJSONObject("data").getJSONArray("stream"));
		return "";
	}

	public static String get(String address) {
		return new YoukuVideo(address).execute();
	}
	
	public static void main(String[] args) {
		YoukuVideo.get("https://acs.youku.com/h5/mtop.youku.play.ups.appinfo.get/1.1/");
		//utid   ckey   current_showid   encryptR_client   preferClarity
		
		
	}
	
	public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }
	public interface JavaScriptInterface{
		public String func();
	}
	
	public String sign(String str) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js"); 
		try {
			InputStream stream = getClass().getClassLoader().getResourceAsStream("awsc.js");
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
			return executeMethod.func();
		}
		throw new RuntimeException("验签失败");
	}
	
	
	//u = s(i.token + "&" + l + "&" + a + "&" + n.data)
	public static String getSign(String token , String l , String appKey , String data) {
		return MD5.create().digestHex(token + "&" + l + "&" + appKey + "&" + data);
	}
}
