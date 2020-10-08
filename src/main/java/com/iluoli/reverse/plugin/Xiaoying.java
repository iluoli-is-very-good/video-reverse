package com.iluoli.reverse.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.hutool.http.HttpResponse;


public class Xiaoying extends BaseExecute{

	public Xiaoying(String url) {
		super(url);
	}

	@Override
	public String execute() {
		HttpResponse response = requestNoHeader(url);
		Document document = Jsoup.parse(response.body());
		String pic = document.getElementsByClass("bigimgtop").get(0).attr("src");
		
		return document.getElementById("videopart").attr("src");
	}

	public static String get(String address) {
		return new Xiaoying(address).execute();
	}
}
