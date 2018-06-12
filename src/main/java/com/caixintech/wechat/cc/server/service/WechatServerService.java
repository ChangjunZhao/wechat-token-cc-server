package com.caixintech.wechat.cc.server.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import com.caixintech.wechat.cc.server.dto.WechatShortUrlDto;
import com.caixintech.wechat.cc.server.types.response.ShortUrlResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.caixintech.wechat.cc.server.dto.WechatAccessTokenDto;
import com.caixintech.wechat.cc.server.dto.WechatTicketDto;
import com.caixintech.wechat.cc.server.util.HttpClientUtil;
import com.google.gson.Gson;

@Service
public class WechatServerService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Gson gson = new Gson();
	
	@Value("${weixin.app.appid}")
	private String appId;
	
	@Value("${weixin.app.secret}")
	private String secret;
	
	private String accessToken;
	
	private String jssdkTicket;
	
	@PostConstruct
	public void updateAccessTokenAndJssdkTicket(){
		WechatAccessTokenDto watd = fetchAccessToken();
		if(watd.getErrcode() == 0){
			setAccessToken(watd.getAccess_token());
		}
		WechatTicketDto wtd = fetchTicket();
		if(wtd.getErrcode() == 0){
			setJssdkTicket(wtd.getTicket());
		}
	}
	
	public Map<String, String> sign(String url) {
		
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + getJssdkTicket() +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        //System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("appId", appId);
        ret.put("jsapi_ticket", getJssdkTicket());
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    public ShortUrlResponse shortenUrl(String longUrl) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

	    String accessToken = getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/shorturl?access_token=%s", accessToken);
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("action", "long2short");
        params.put("long_url", longUrl);
        HttpPost postRequest = new HttpPost(url);
        String body = objectMapper.writeValueAsString(params);
        logger.debug("shorten url body: {}", body);
        postRequest.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = client.execute(postRequest)) {
            String result = EntityUtils.toString(response.getEntity());
            logger.debug("url shorten result: {}", result);
            WechatShortUrlDto wechatShortUrlDto = objectMapper.readValue(result, WechatShortUrlDto.class);
            return new ShortUrlResponse(wechatShortUrlDto.getShort_url());
        } finally {
            client.close();
        }
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
    private WechatAccessTokenDto fetchAccessToken() {  
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+secret;  
        String result = HttpClientUtil.httpGet(url, "UTF-8");
        logger.debug("getAccessToken origin result:{}",result);
		return gson.fromJson(result, WechatAccessTokenDto.class);
    } 
    
    private WechatTicketDto fetchTicket(){
    	logger.debug("更新tickets");
    	String accessToken = getAccessToken();
    	String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
    	String result = HttpClientUtil.httpGet(url, "UTF-8");
    	logger.debug("getTicket origin result:{}",result);
 		return gson.fromJson(result, WechatTicketDto.class);
    }

	public String getJssdkTicket() {
		return jssdkTicket;
	}

	public void setJssdkTicket(String jssdkTicket) {
		this.jssdkTicket = jssdkTicket;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}
	
	
}
