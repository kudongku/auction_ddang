package com.ip.ddangddangddang.global.s3;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    String upload(MultipartFile auctionImage, String keyName) throws IOException;

    void delete(String keyName);

    String getPresignedURL(String keyName);
}
