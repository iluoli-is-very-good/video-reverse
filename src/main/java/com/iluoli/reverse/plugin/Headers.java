package com.iluoli.reverse.plugin;

import java.util.HashMap;
import java.util.Map;

public class Headers {

	
	public static Map<String , String> value;
	
	
	
	static {
		if(value == null) {
			value = new HashMap<>();
			value.put("accept-encoding", "gzip, deflate, br");
			value.put("accept-language", "zh-CN,zh;q=0.9");
			value.put("pragma", "no-cache");
			value.put("cache-control", "no-cache");
			value.put("upgrade-insecure-requests", "1");
			value.put("user-agent", "Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Mobile Safari/537.36");
		}
	}
	
}
