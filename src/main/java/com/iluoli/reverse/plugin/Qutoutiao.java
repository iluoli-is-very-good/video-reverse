package com.iluoli.reverse.plugin;

import java.net.URLDecoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpResponse;

public class Qutoutiao extends BaseExecute{


	
	public Qutoutiao(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String realUrl = getRealPlayAddress(url);
		
		realUrl = realUrl.split("&jsonp=")[1].split("&")[0];
		realUrl = "http:" + URLDecoder.decode(realUrl);
		HttpResponse response = request(realUrl);
		String body = UnicodeUtil.toString(response.body()).replaceAll("cb\\(", "").replaceAll("\\)", "");
		JSONObject object = JSONObject.parseObject(body);
		
		JSONObject detail = JSONObject.parseObject(object.getString("detail").replaceAll("\\\\", ""));
		String address = detail.getString("address");
		try {
			JSONArray addresss = JSONObject.parseArray(address);
			for(Object addr : addresss) {
				JSONObject parseObject = JSONObject.parseObject(addr.toString());
				if("hd".equals(parseObject.getString("definition"))) {
					return "http://v4.qutoutiao.net/" + parseObject.getString("url");
				}
			}
		} catch (Exception e) {
			
		}
		return "http://v5.qutoutiao.net/" + address;
	}
	
	public static String get(String address) {
		return new Qutoutiao(address).execute();
	}

}
