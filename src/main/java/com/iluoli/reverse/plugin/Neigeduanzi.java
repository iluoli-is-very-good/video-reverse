package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpResponse;

public class Neigeduanzi extends BaseExecute{

	public Neigeduanzi(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String[] split = url.split("/");
		String vid = split[split.length - 1];
		String sign = "GETfunny.duodianxingkong.com/api/funny-api/feed/h5?agentType=18&agentVersion=1.0.1&feedId=" + vid + "D3f12e6223c12a253a3a";
		sign = MD5.create().digestHex(sign);
		String requestUrl = "https://funny.duodianxingkong.com/api/funny-api/feed/h5?agentType=18&agentVersion=1.0.1&feedId=" + vid + "&sign=" + sign;
		HttpResponse response = requestNoHeader(requestUrl);
		JSONObject json = JSONObject.parseObject(response.body());
		JSONObject video = json.getJSONObject("data").getJSONObject("feedH5Vo");
		String title = video.getString("content");
		String pic = video.getJSONObject("video").getString("videoCover");
		return video.getJSONObject("video").getString("videoUrl");
	}

	public static String get(String address) {
		return new Neigeduanzi(address).execute();
	}
	
	public static void main(String[] args) {
		System.out.println(Neigeduanzi.get("https://clply.cn/common/share-url/#/120520245"));
	}
}
