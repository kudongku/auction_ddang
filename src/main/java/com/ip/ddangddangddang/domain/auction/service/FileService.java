package com.ip.ddangddangddang.domain.auction.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String upload(MultipartFile auctionImage, String objectName) throws IOException;

    void delete(String keyName);

    String getPresignedURL(String keyName);
}
