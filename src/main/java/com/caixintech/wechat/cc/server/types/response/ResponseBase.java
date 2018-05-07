package com.caixintech.wechat.cc.server.types.response;

import com.caixintech.wechat.cc.server.types.exception.Errno;

import java.util.UUID;

public class ResponseBase {
    private Errno errno;
    private String detail;
    private String requestId = UUID.randomUUID().toString();

    protected ResponseBase() {
        this.errno = Errno.NOTHING;
    }

    public ResponseBase(Errno errno, String detail) {
        this.errno = errno;
        this.detail = detail;
    }

    public Errno getErrno() {
        return errno;
    }

    public void setErrno(Errno errno) {
        this.errno = errno;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
