package com.ip.ddangddangddang.global.exception.custom;

import com.amazonaws.services.s3.model.AmazonS3Exception;

public class EmptyImageException extends AmazonS3Exception {

    public EmptyImageException(String message) {
        super(message);
    }
}
