package com.iluoli.reverse.plugin;


import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

public class Bixin extends BaseExecute{

	public Bixin(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String dynamic_id = UrlUtil.parse(url).params.get("dynamic_id");
		
		HttpRequest post = HttpUtil.createPost("https://h5.hibixin.com/bixin/time-line/v2/detail");
		post.body("{\"timelineId\":\"" + dynamic_id + "\"}");
		String body = post.execute().body();
		JSONObject json = JSONObject.parseObject(body);
		JSONObject video = json.getJSONObject("result").getJSONObject("timeLineDetail").getJSONObject("videoInfoDTO");
		
		String pic = video.getString("videoFirstImg");
		String title = json.getJSONObject("result").getJSONObject("timeLineDetail").getString("content");
		return video.getString("videoUrl");
	}
	
	public static String get(String address) {
		return new Bixin(address).execute();
	}

}
