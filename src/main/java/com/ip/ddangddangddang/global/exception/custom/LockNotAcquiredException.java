package com.ip.ddangddangddang.global.exception.custom;

public class LockNotAcquiredException extends RuntimeException {

    public LockNotAcquiredException(final String message) {
        super(message);
    }

}
