package com.caixintech.wechat.cc.server.types.exception;

public class UnexpectedException extends ExceptionBase {
    public UnexpectedException(String detail) {
        super(Errno.ERRNO_CRITICAL, detail);
    }
}
