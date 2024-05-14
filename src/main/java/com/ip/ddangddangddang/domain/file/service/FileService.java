package com.ip.ddangddangddang.domain.file.service;

import com.ip.ddangddangddang.domain.file.dto.response.FileCreateResponseDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileReadResponseDto;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.file.repository.FileRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.EmptyImageException;
import com.ip.ddangddangddang.global.s3.FileUploadService;
import java.io.IOException;
import java.util.Optional;
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
    private final FileUploadService fileUploadService;
    private final UserService userService;

    @Transactional
    public FileCreateResponseDto upload(MultipartFile auctionImage, String objectName, Long userId) {

        if (auctionImage.isEmpty()) {
            throw new EmptyImageException("이미지가 존재하지 않습니다.");
        }

        User user = userService.getUserByIdOrElseThrow(userId);
        String keyName = createKeyName(objectName);

        try {
            String filePath = fileUploadService.upload(auctionImage, keyName);
            Long fileId = fileRepository.save(new File(objectName, keyName, filePath, user)).getId();
            return new FileCreateResponseDto(fileId);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

    }

    @Transactional
    public void delete(File file) {
        fileUploadService.delete(file.getKeyName());
        fileRepository.delete(file);
    }

    public FileReadResponseDto getPresignedURL(Long fileId, Long userId) {
        File file = findFileOrElseThrow(fileId);

        if (!file.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
        }

        String preSignedUrl = fileUploadService.getPresignedURL(file.getKeyName());
        return new FileReadResponseDto(file.getId(), preSignedUrl);
    }

    public File findFileOrElseThrow(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(
            () -> new NullPointerException("없는 이미지 입니다.")
        );
    }

    public Optional<File> getFileById(Long fileId) {
        return fileRepository.findById(fileId);
    }

    private String createKeyName(String objectName) {
        String uuid = UUID.randomUUID().toString();
        String extension = objectName.replaceAll(" ", ""); // 공백제거 옥 장 판
        return extension + uuid;
    }

}
