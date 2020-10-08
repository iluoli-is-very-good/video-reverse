package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;
import com.iluoli.reverse.common.utils.UrlUtil.UrlEntity;

import cn.hutool.http.HttpResponse;


public class Inke extends BaseExecute{

	public Inke(String url) {
		super(url);
	}

	@Override
	public String execute() {
		UrlEntity urlEntity = UrlUtil.parse(url);
		HttpResponse response = requestNoHeader("https://service.inke.cn/api/v2/feed/show?feed_id="+ urlEntity.params.get("feed_id") +"&uid=" + urlEntity.params.get("uid"));
		JSONObject object = JSONObject.parseObject(response.body());
		JSONObject jsonObject = object.getJSONObject("data").getJSONObject("feed_info").getJSONObject("content").getJSONArray("attachments").getJSONObject(0).getJSONObject("data");
		
		String pic = jsonObject.getString("cover");
		return jsonObject.getString("url");
	}
	
	
	public static String get(String address) {
		return new Inke(address).execute();
	}
	
	
	public static void main(String[] args) {
		System.out.println(Inke.get("https://boc.inke.cn/innerapp/feed-share/index.html?from=feed&inkewtype=web&inkewid=feed_share_h5&inkewname=feed_share_h5_201804&feed_id=158159190600001109&uid=732543109"));
	}

}
