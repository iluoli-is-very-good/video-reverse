package com.iluoli.reverse.plugin;

import cn.hutool.http.HttpResponse;

public class Weishi extends BaseExecute{

	public Weishi(String url) {
		super(url);
	}

	public static String VIDEOSTART = "video_url\":\"";
	public static String VIDEOEND = "\",\"material_thumburl";
	
	

	@Override
	public String execute() {
		HttpResponse response = requestNoHeader(url + "&from=pc&orifrom=");
		String body = response.body();
		int start = body.indexOf(VIDEOSTART);
		int end = body.indexOf(VIDEOEND);
		
		return body.substring(start + VIDEOSTART.length(), end);
	}

	public static String get(String address) {
		return new Weishi(address).execute();
	}
}
