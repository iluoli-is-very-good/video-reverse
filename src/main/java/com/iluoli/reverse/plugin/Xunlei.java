package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;

import cn.hutool.http.HttpResponse;

public class Xunlei extends BaseExecute{

	public Xunlei(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String vid = UrlUtil.parse(url).params.get("id");
		HttpResponse response = requestNoHeader("https://api-xl9-ssl.xunlei.com/sl/ivideo_v5/info?movieid=" + vid);
		JSONObject jsonObject = JSONObject.parseObject(response.body());
		JSONObject videoInfo = jsonObject.getJSONObject("video_info");
		videoInfo.getString("cover_url");
		return videoInfo.getString("play_url");
	}

	public static String get(String address) {
		return new Xunlei(address).execute();
	}
}
