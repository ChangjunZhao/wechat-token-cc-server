package com.caixintech.wechat.cc.server.api;

import com.caixintech.wechat.cc.server.types.exception.Errno;
import com.caixintech.wechat.cc.server.types.exception.ExceptionBase;
import com.caixintech.wechat.cc.server.types.response.ResponseBase;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvise {
    @ExceptionHandler(ExceptionBase.class)
    public ResponseBase processExceptionBase(ExceptionBase exceptionBase) {
        return new ResponseBase(exceptionBase.getErrno(), exceptionBase.getDetai());
    }

    @ExceptionHandler(Exception.class)
    public ResponseBase processException(Exception exception) {
        return new ResponseBase(Errno.ERRNO_CRITICAL, exception.getMessage());
    }
}
