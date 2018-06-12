package com.caixintech.wechat.cc.server.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.caixintech.wechat.cc.server.types.response.ShortUrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caixintech.wechat.cc.server.service.WechatServerService;

@RestController
@RequestMapping("/wechat")
public class WechatController {
	
	@Autowired
	WechatServerService wechatServerService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * JS-SDK使用URL签名算法 详见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141115
	 * 
	 * @param url 需要签名的URL
	 * @return 返回如下json：
	 * {"signature":"a935ba6425af0200f9c1d235afceb226c8aa943e",
	 * "appId":"wx809541fa44664742",
	 * "jsapi_ticket":"HoagFKDcsGMVCIY2vOjf9gp3KggvsLK2CZlPyty3efYZ7mjaoVTAZzQnP-mrf8ih-hih3vq-0vS8WIMr6xXvzA",
	 * "url":"http://wxtest.devincloud.cn/",
	 * "nonceStr":"c9a6017c-bb34-4339-a3c1-7430e1fbd7ea",
	 * "timestamp":"1499067696"}
	 */
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public ResponseEntity<?> sign(@RequestParam String url){
		logger.debug("request jssdk sign:{}",url);
		try {
			return ResponseEntity.ok(wechatServerService.sign(url));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	/**
	 * 获取Access_token，详见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183
	 * @return 返回access token，格式如下:
	 * {"token":"vx-z0qrVAlJR3E7neCH9U8kwX3Jvgmif5cPMm_Usxs22OUhzR3GKtkM2Q6t4XDmRKWfYslRHZIqNzIm6SX1LmYE9WhRzl4f0oqaFpB6hE-wk3PcjHGQKGF7yrF-2G9j8WJOdABAIVA"}
	 */
	@RequestMapping(value = "/accesstoken", method = RequestMethod.GET)
	public ResponseEntity<?> getAccessToken(){
		logger.debug("request access token");
		Map<String, String> tokenMap = new HashMap<String,String>();
		tokenMap.put("token", wechatServerService.getAccessToken());
		return ResponseEntity.ok(tokenMap);
	}

	@RequestMapping(path = "/url-shorter", method = RequestMethod.POST)
    public ShortUrlResponse shortenUrl(@RequestParam(name = "longUrl") String longUrl) throws IOException {
	    return wechatServerService.shortenUrl(longUrl);
    }
}
