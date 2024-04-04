package com.ip.ddangddangddang.domain.auction.service;

import com.ip.ddangddangddang.domain.auction.entity.File;
import com.ip.ddangddangddang.domain.auction.repository.FileRepository;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final S3Service s3Service;

    @Transactional
    public File upload(MultipartFile auctionImage, String objectName) throws IOException {
        String keyName = createKeyName(objectName);
        String filePath = s3Service.upload(auctionImage, keyName);
        return fileRepository.save(new File(filePath, keyName, objectName));
    }

    @Transactional
    public void delete(String fileKeyName) {
        s3Service.delete(fileKeyName);
    }

    private String createKeyName(String objectName) {
        String uuid = UUID.randomUUID().toString();
        String extension = objectName.replaceAll(" ", "");
        return extension + uuid;
    }

}
