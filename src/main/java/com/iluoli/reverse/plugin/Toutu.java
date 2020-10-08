package com.iluoli.reverse.plugin;

import java.net.URLDecoder;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;

import cn.hutool.http.HttpResponse;

public class Toutu extends BaseExecute{

	public Toutu(String url) {
		super(url);
	}

	@Override
	public String execute() {
		url = URLDecoder.decode(url);
		String contendid = UrlUtil.parse(url).params.get("contendid");
		
		HttpResponse response = request(url);
		String body = response.body();
		String api = "";
		if(body.indexOf("内含段友") != -1) {
			api = "https://api.itoutu.com:8899/NHDZSEVER/content/sharecontent.do?contentid=" + contendid;
		}else if(body.indexOf("头图") != -1) {
			api = "https://api.itoutu.com:8898/CTKJSEVER/content/sharecontent.do?contentid=" + contendid;
		}else {
			throw new RuntimeException("未知的【头图】视频类型");
		}
		HttpResponse response2 = requestNoHeader(api);
		JSONObject json = JSONObject.parseObject(response2.body());
		JSONObject data = json.getJSONObject("data");
		String title = data.getString("contenttitle");
		String contenturllist = data.getString("contenturllist");
		List<String> list = JSONArray.parseArray(contenturllist, String.class);
		String pic = list.get(0);
		return list.get(1);
	}

	public static String get(String address) {
		return new Toutu(address).execute();
	}
	
	public static void main(String[] args) {
		System.out.println(Toutu.get("https://v3.toutushare.com/share/shareindex.html?contendid=fdd7a20fd3194a3993708989f07cba8b"));
	}
}
