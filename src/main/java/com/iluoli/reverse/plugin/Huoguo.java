package com.iluoli.reverse.plugin;

import com.alibaba.fastjson.JSONObject;
import com.iluoli.reverse.common.utils.UrlUtil;

import cn.hutool.http.HttpResponse;

public class Huoguo extends BaseExecute{

	public Huoguo(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String vid = UrlUtil.parse(url).params.get("id");
		
		HttpResponse result = request("https://vv.video.qq.com/getinfo?callback=1&platform=11001&otype=json&nocache=0&_rnd=1581758939&show1080p=0&vids=" + vid);
		JSONObject jsonObject = JSONObject.parseObject(result.body().replaceAll("1\\(", "").replaceAll("\\)", ""));
		JSONObject vi = jsonObject.getJSONObject("vl").getJSONArray("vi").getJSONObject(0);
		String fvkey= vi.getString("fvkey");
		String fn= vi.getString("fn");
		String url = vi.getJSONObject("ul").getJSONArray("ui").getJSONObject(0).getString("url");
		
		return url + fn + "?vkey=" + fvkey;
	}

	public static String get(String address) {
		return new Huoguo(address).execute();
	}
	
	public static void main(String[] args) {
		System.out.println(Huoguo.get("https://huoguo.qq.com/m/video.html?id=k3063169419&ptag=huoguo&first=1"));
	}
}
