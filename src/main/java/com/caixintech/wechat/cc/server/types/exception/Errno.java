package com.caixintech.wechat.cc.server.types.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Errno {
    NOTHING(0),
    ERRNO_CRITICAL(500);

    private long code;
    Errno(long code) {
        this.code = code;
    }

    @JsonValue
    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}
