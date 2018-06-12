package com.caixintech.wechat.cc.server.types.response;

public class ShortUrlResponse extends ResponseBase {
    private String shortUrl;

    public ShortUrlResponse(String shortUrl) {
        super();
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
