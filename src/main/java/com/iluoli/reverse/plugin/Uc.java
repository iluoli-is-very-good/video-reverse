package com.iluoli.reverse.plugin;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;

import cn.hutool.http.HttpResponse;

public class Uc extends BaseExecute{

	public Uc(String url) {
		super(url);
	}

	@Override
	public String execute() {
		Map<String, String> params = UrlUtil.parse(url).params;
		HttpResponse response = request(url);
		HttpResponse res1 = request("https://ff.dayu.com/contents/origin/" + params.get("wm_aid") + "?biz_id=1002&_fetch_author=1&_incr_fields=click1,click2,click3,click_total,play,like");
		String body1 = res1.body();
		JSONObject json1 = JSONObject.parseObject(body1);
		JSONObject data = json1.getJSONObject("data");
		String wm_cid = data.getString("content_id");
		String ums_id = data.getJSONObject("body").getJSONArray("videos").getJSONObject(0).getString("ums_id");

		HttpResponse result = request("https://mparticle.uc.cn/api/vps?token=" + response.getCookieValue("vpstoken") + "&ums_id= " + ums_id + "&wm_cid= " + wm_cid + " &wm_id=" + params.get("wm_id") + "&resolution=high");
		
		String url = JSONObject.parseObject(result.body()).getJSONObject("data").getString("url");
		String pic = data.getString("cover_url");
		return url;
	}
	
	public static String get(String address) {
		return new Uc(address).execute();
	}
}
