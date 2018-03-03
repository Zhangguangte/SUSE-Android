package com.agmcs.ssuussee;

/**
 * Created by agmcs on 2015/5/25.
 */
public class CommonException extends Exception {
    public CommonException() {
        super();
    }

    public CommonException(String detailMessage) {
        super(detailMessage);
    }

    public CommonException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CommonException(Throwable throwable) {
        super(throwable);
    }
}
