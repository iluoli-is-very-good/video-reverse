package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;

import cn.hutool.http.HttpResponse;

public class Kaiyan extends BaseExecute{

	public Kaiyan(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String vid = UrlUtil.parse(url).params.get("vid");
		HttpResponse response = request("https://baobab.kaiyanapp.com/api/v1/video/" + vid);
		JSONObject json = JSONObject.parseObject(response.body());
		String pic = json.getString("coverForDetail");
		return json.getString("playUrl");
	}
	
	
	public static String get(String address) {
		return new Kaiyan(address).execute();
	}
}
