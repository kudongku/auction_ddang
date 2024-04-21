package com.ip.ddangddangddang.global.exception.custom;

public class UserHasNotAuthorityToFileException extends RuntimeException {

    public UserHasNotAuthorityToFileException(String message) {
        super(message);
    }
}
