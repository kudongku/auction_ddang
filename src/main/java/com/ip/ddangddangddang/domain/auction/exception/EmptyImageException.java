package com.ip.ddangddangddang.domain.auction.exception;

import com.amazonaws.services.s3.model.AmazonS3Exception;

public class EmptyImageException extends AmazonS3Exception {

    public EmptyImageException(String message) {
        super(message);
    }
}
