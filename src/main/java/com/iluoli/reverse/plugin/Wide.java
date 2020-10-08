package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;

import cn.hutool.http.HttpResponse;

public class Wide extends BaseExecute{

	public Wide(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String vid = UrlUtil.parse(url).params.get("video_id");
		HttpResponse response = requestNoHeader("https://api.wide.meipai.com/h5/video_show.json?video_id=" + vid);
		JSONObject jsonObject = JSONObject.parseObject(response.body());
		JSONObject object = jsonObject.getJSONObject("response").getJSONObject("video_info");
		
		object.getString("title");
		object.getString("cover");
		return object.getString("source");
	}

	public static String get(String address) {
		return new Wide(address).execute();
	}
}
