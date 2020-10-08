package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Douyin extends BaseExecute{

	public Douyin(String url) {
		super(url);
	}

	public static String DOUYINSTART = "dytk: \"";
	public static String DOUYINEND = "\" });";
	
	@Override
	public String execute() {
		String realUrl = getRealPlayAddress(url);
		log.info(realUrl);
		HttpResponse httpResponse = request(realUrl);
		String body = httpResponse.body();
		int start = body.indexOf(DOUYINSTART);
		int end = body.indexOf(DOUYINEND);
		
		String item_id = realUrl.split("/")[5];
		String sign = body.substring(start + DOUYINSTART.length(), end);
		log.info("item_id:{} , sign:{}" , item_id , sign);
		
		
		HttpResponse response = requestNoHeader("https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids="+item_id+"&dytk=" + sign);
		log.info(response.body().toString());
		JSONObject result = JSONObject.parseObject(response.body());
		log.info(result.toString());
		String url = result.getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").getString(0);
		return getRealPlayAddress(url);
	}
	

	public static String get(String address) {
		return new Douyin(address).execute();
	}
}
