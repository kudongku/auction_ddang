package com.ip.ddangddangddang.domain.auction.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ip.ddangddangddang.domain.auction.exception.NotValidBucketException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile, String keyName) throws IOException {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(multipartFile.getInputStream().available());
        // TODO: 4/5/24 메타데이터에 사진의 용량 설정

        try{
            amazonS3.putObject(bucket, keyName, multipartFile.getInputStream(), objMeta);
        }catch (AmazonS3Exception e){
            throw new NotValidBucketException("존재하지 않는 버킷입니다.");
        }

        return URLDecoder.decode(
            amazonS3.getUrl(bucket, keyName).toString(),
            StandardCharsets.UTF_8
        );
    }

    public void delete(String keyName) {
        keyName = "noname";
        amazonS3.deleteObject(bucket, keyName);
        // TODO: 4/5/24 삭제하려고 했는데 없을 경우, 예외처리
    }

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

