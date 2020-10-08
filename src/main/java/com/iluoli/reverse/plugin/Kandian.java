package com.iluoli.reverse.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpResponse;

public class Kandian extends BaseExecute{

	public Kandian(String url) {
		super(url);
	}

	@Override
	public String execute() {
		HttpResponse response = request(url);
		Document parse = Jsoup.parse(response.body());
		Element video_play = parse.getElementById("video-play");
		String vid = video_play.attr("data-video-src");
		HttpResponse result = request("http://h5vv.video.qq.com/getinfo?callback=1&platform=11001&otype=json&nocache=0&_rnd=1581758939&vids=" + vid);
		JSONObject jsonObject = JSONObject.parseObject(result.body().replaceAll("1\\(", "").replaceAll("\\)", ""));
		JSONObject vi = jsonObject.getJSONObject("vl").getJSONArray("vi").getJSONObject(0);
		String fvkey= vi.getString("fvkey");
		String fn= vi.getString("fn");
		String url = vi.getJSONObject("ul").getJSONArray("ui").getJSONObject(0).getString("url");
		
		return url + fn + "?vkey=" + fvkey;
	}

	public static String get(String address) {
		return new Kandian(address).execute();
	}
}
