package com.ip.ddangddangddang.domain.file.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    void upload(MultipartFile auctionImage, String keyName) throws IOException;

    void delete(String keyName);

    String getPresignedURL(String keyName);
}
