package com.iluoli.reverse.plugin;

import cn.hutool.http.HttpResponse;

public class Huoshan extends BaseExecute{

	static String HuoshanSTART = "item_id=";
	static String HuoshanEND = "&tag=";
	
	static String HuoshanSTART2 = "video_id=";
	static String HuoshanEND2 = "&line=";
	
	public Huoshan(String url) {
		super(url);
	}
	
	@Override
	public String execute() {
		String realUrl = getRealPlayAddress(url);
		int start = realUrl.indexOf(HuoshanSTART);
		int end = realUrl.indexOf(HuoshanEND);
		String item_id = realUrl.substring(start + HuoshanSTART.length(), end);
		
		HttpResponse response = requestNoHeader("https://share.huoshan.com/api/item/info?item_id=" + item_id);
		String body = response.body();
		
		int start2 = body.indexOf(HuoshanSTART2);
		int end2 = body.indexOf(HuoshanEND2);
		String video_id = body.substring(start2 + HuoshanSTART2.length(), end2);
		return getRealPlayAddress("http://hotsoon.snssdk.com/hotsoon/item/video/_playback/?video_id=" + video_id);
	}

	public static String get(String address) {
		return new Huoshan(address).execute();
	}

	
	
}
