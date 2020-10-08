package com.iluoli.reverse.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cn.hutool.http.HttpResponse;


public class Zuiyou extends BaseExecute{

//	static String START = "<video src=\"";
//	static String END = "\" webkit-playsinline=\"true\"";
	
	public Zuiyou(String url) {
		super(url);
	}

	@Override
	public String execute() {
		HttpResponse response = requestNoHeader("https://share.izuiyou.com" + getRealPlayAddress(url));
		String body = response.body();
		Document document = Jsoup.parse(body);
		Elements video = document.getElementsByClass("ImageBox__video--single").get(0).getElementsByTag("video");
		String pic = video.attr("poster");
		return video.attr("src");
	}

	public static String get(String address) {
		return new Zuiyou(address).execute();
	}
}
