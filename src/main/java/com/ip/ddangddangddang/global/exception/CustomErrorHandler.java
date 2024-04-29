package com.ip.ddangddangddang.global.exception;

import org.springframework.util.ErrorHandler;

public class CustomErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        // 로그 기록, 알림 전송, etc.
        System.err.println("Error in Redis message listener: " + t.getMessage());
    }
}
