package com.ip.ddangddangddang.global.exception.customedExceptions;

import com.amazonaws.services.s3.model.AmazonS3Exception;

public class NotValidBucketException extends AmazonS3Exception {

    public NotValidBucketException(String message) {
        super(message);
    }

}
