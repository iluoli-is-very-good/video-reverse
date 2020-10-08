package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpResponse;

public class Pipixia extends BaseExecute{

	public Pipixia(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String realUrl = getRealPlayAddress(url);
		
		String item_id = realUrl.split("/")[4].split("\\?")[0];
		
		HttpResponse response = request("https://h5.pipix.com/bds/webapi/item/detail/?item_id="+ item_id +"&source=share");
		String body = response.body();
		
		return JSONObject.parseObject(body).getJSONObject("data").getJSONObject("item").getJSONObject("origin_video_download").getJSONArray("url_list").getJSONObject(0).getString("url");
	}

	
	public static String get(String address) {
		return new Pipixia(address).execute();
	}
}
