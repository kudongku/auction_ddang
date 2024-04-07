package com.ip.ddangddangddang.domain.file.service;

import com.ip.ddangddangddang.domain.file.dto.response.FileReadResponseDto;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.file.repository.FileRepository;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.EmptyImageException;
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

    private final S3Service s3Service;
    private final UserService userService;
    private final FileRepository fileRepository;

    @Transactional
    public Long upload(MultipartFile auctionImage, String objectName, Long userId)
        throws IOException {
        userService.getUser(userId);

        if (auctionImage.isEmpty()) {
            throw new EmptyImageException("이미지가 존재하지 않습니다.");
        }

        String keyName = createKeyName(objectName);
        s3Service.upload(auctionImage, keyName);

        return fileRepository.save(new File(objectName, keyName, userId)).getId();
    }

    @Transactional
    public void delete(Long fileId, Long userId) {
        File file = fileRepository.findById(fileId).orElseThrow(
            ()-> new NullPointerException("없는 이미지 입니다.")
        );

        if(!file.getUserId().equals(userId)){
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
        }

        s3Service.delete(file.getKeyName());
        fileRepository.delete(file);
    }

    public FileReadResponseDto getPresignedURL(Long fileId, Long userId) {
        File file = fileRepository.findById(fileId).orElseThrow(
            ()-> new NullPointerException("없는 이미지 입니다.")
        );

        if(!file.getUserId().equals(userId)){
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
        }

        String preSignedUrl = s3Service.getPresignedURL(file.getKeyName());
        return new FileReadResponseDto(preSignedUrl);
    }

    private String createKeyName(String objectName) {
        String uuid = UUID.randomUUID().toString();
        String extension = objectName.replaceAll(" ", "");
        return extension + uuid;
    }

}
