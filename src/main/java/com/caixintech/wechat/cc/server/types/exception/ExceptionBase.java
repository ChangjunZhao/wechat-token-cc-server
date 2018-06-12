package com.caixintech.wechat.cc.server.types.exception;

public class ExceptionBase extends RuntimeException {
    private Errno errno;
    private String detai;

    protected ExceptionBase(Errno errno, String detail) {
        this.errno = errno;
        this.detai = detail;
    }

    public Errno getErrno() {
        return errno;
    }

    public void setErrno(Errno errno) {
        this.errno = errno;
    }

    public String getDetai() {
        return detai;
    }

    public void setDetai(String detai) {
        this.detai = detai;
    }
}
