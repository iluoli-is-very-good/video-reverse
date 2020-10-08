package com.iluoli.reverse.plugin;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

public class Xiaokaxiu extends BaseExecute{

	public Xiaokaxiu(String url) {
		super(url);
	}
	
	@Override
	public String execute() {
		String id = url.substring(url.indexOf("?id=") + 4);
		Long time = new Date().getTime() / 1000;
		HttpRequest request = HttpUtil.createGet("https://appapi.xiaokaxiu.com/api/v1/web/share/video/" + id + "?time=" + time);
		request.header("x-sign", getSign(time));
		HttpResponse execute = request.execute();
		String body = execute.body();
		JSONObject video = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("video");
		String pic = video.getString("cover");
		String title = video.getString("title");
		return video.getJSONArray("url").getString(0);
	}
	
	public static String get(String address) {
		return	new Xiaokaxiu(address).execute();
	}

	
	public static String getSign(Long times) {
		return MD5.create().digestHex("S14OnTD#Qvdv3L=3vm&time=" + times);
	}
	
	public static void main(String[] args) {
		System.out.println(Xiaokaxiu.get("https://mobile.xiaokaxiu.com/video?id=121865809"));
	}

	
}
