package com.iluoli.reverse.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpResponse;

public class Quanminkg extends BaseExecute{

	static String START = "window.__DATA__ = ";
	static String END = "; </script>";
	
	public Quanminkg(String url) {
		super(url);
	}

	@Override
	public String execute() {
		
		HttpResponse response = request(getRealPlayAddress(url));
		String body = response.body();
		String result = body.substring(body.indexOf(START) + START.length(), body.indexOf(END));
		JSONObject json = JSONObject.parseObject(result);
		JSONObject detail = json.getJSONObject("detail");
		return detail.getString("playurl_video");
	}

	
	public static String get(String address) {
		return new Quanminkg(address).execute();
	}
}
