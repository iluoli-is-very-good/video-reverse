package com.iluoli.reverse.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.hutool.http.HttpResponse;


public class Lishipin extends BaseExecute{
	
	public String START = "srcUrl=\"";
	
	public String END = "\",vdoUrl=srcUrl";

	public Lishipin(String url) {
		super(url);
	}

	@Override
	public String execute() {
		HttpResponse response = requestNoHeader(url);
		String body = response.body();
		Document parse = Jsoup.parse(body);
		System.out.println(parse.getElementById("poster").getElementsByClass("img").get(0).attr("src"));
		
		return body.substring(body.indexOf(START) + START.length(), body.indexOf(END));
	}
	
	public static String get(String address) {
		return new Lishipin(address).execute();
	}
}
