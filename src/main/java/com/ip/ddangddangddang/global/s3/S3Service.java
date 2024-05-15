package com.ip.ddangddangddang.global.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ip.ddangddangddang.global.exception.customedExceptions.NotValidBucketException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class S3Service implements FileUploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public String upload(MultipartFile auctionImage, String keyName) throws IOException {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(auctionImage.getContentType());
        objMeta.setContentLength(auctionImage.getInputStream().available());

        try {
            amazonS3.putObject(bucket, keyName, auctionImage.getInputStream(), objMeta);
        } catch (AmazonS3Exception e) {
            throw new NotValidBucketException("존재하지 않는 버킷입니다.");
        }

        return "https://dccg5mv6uel89.cloudfront.net/" + keyName;
    }

    @Override
    public void delete(String keyName) {
        try {
            amazonS3.deleteObject(bucket, keyName);
        } catch (AmazonS3Exception e) {
            throw new NotValidBucketException("존재하지 않는 버킷입니다.");
        }
    }

    @Override
    public String getPresignedURL(String keyName) {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 2);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucket, keyName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL presignedURL = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return presignedURL.toString();
    }
}
