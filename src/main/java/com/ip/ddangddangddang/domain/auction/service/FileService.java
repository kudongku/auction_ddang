package com.ip.ddangddangddang.domain.auction.service;

import com.ip.ddangddangddang.domain.auction.entity.File;
import com.ip.ddangddangddang.domain.auction.exception.EmptyImageException;
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

        if(auctionImage.isEmpty()){
            throw new EmptyImageException("이미지가 존재하지 않습니다.");
        }

        String keyName = createKeyName(objectName);
        String filePath = s3Service.upload(auctionImage, keyName);
        return fileRepository.save(new File(filePath, keyName, objectName));
    }

    @Transactional
    public void delete(String fileKeyName, Long fileId) {
        s3Service.delete(fileKeyName);
        fileRepository.deleteById(fileId);
    }

    public String getPresignedURL(String fileKeyName) {
        return s3Service.getPresignedURL(fileKeyName);
        // TODO: 4/6/24 조회를 하지 않는 FileEntity 있어야 할까
    }

    private String createKeyName(String objectName) {
        String uuid = UUID.randomUUID().toString();
        String extension = objectName.replaceAll(" ", "");
        return extension + uuid;
    }
}
