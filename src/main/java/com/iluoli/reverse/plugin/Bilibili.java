package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpResponse;

public class Bilibili extends BaseExecute{

	public static String START = "window.__INITIAL_STATE__=";
	
	public static String END = ";\n" + 
			"    if(window.__INITIAL_STATE__.abserver) {";
	
	public Bilibili(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String realPlayAddress = getRealPlayAddress(getRealPlayAddress(url));
		HttpResponse response = request(realPlayAddress);
		String body = response.body();
		String result = body.substring(body.indexOf(START) + START.length(), body.indexOf(END));
		JSONObject jsonObject = JSONObject.parseObject(result);
		JSONObject videoInfo = jsonObject.getJSONObject("reduxAsyncConnect").getJSONObject("videoInfo");
		videoInfo.getString("pic");
		return videoInfo.getString("initUrl");
	}
	
	public static String get(String address) {
		return new Bilibili(address).execute();
	}
	public static void main(String[] args) {
		System.out.println(Bilibili.get("https://b23.tv/av84665661"));
	}
}
