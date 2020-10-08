package com.iluoli.reverse.plugin;


import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseExecute {
	
	String url;
	
	
	
	
	public BaseExecute(String url) {
		super();
		this.url = url;
	}

	public abstract String execute();
	
	
	
	/**
	 * 有的可能经过302重定向了
	 * @param url
	 * @return
	 */
	String getRealPlayAddress(String url) {
		HttpResponse response = request(url);
		if(response.getStatus() == 302) {
			return response.header("Location");
		}
		if(response.getStatus() == 301) {
			return response.header("Location");
		}
		return url;
	}
	
	
	HttpResponse request(String url) {
		return HttpUtil.createGet(url).addHeaders(Headers.value).execute();
	}
	
	HttpResponse requestNoHeader(String url) {
		return HttpUtil.createGet(url).execute();
	}
}
