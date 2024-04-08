package com.ip.ddangddangddang.domain.file.service;

import com.ip.ddangddangddang.domain.file.dto.response.FileReadResponseDto;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.file.repository.FileRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
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

    private final FileUploadService fileUploadService;
    private final UserService userService;
    private final FileRepository fileRepository;

    @Transactional
    public Long upload(MultipartFile auctionImage, String objectName, Long userId) {
        User user = userService.findUserOrElseThrow(userId);

        if (auctionImage.isEmpty()) {
            throw new EmptyImageException("이미지가 존재하지 않습니다.");
        }

        String keyName = createKeyName(objectName);

        try{
            fileUploadService.upload(auctionImage, keyName);
        }catch(IOException e){
            throw new IllegalArgumentException();
        }


        return fileRepository.save(new File(objectName, keyName, user)).getId();
    }

    @Transactional
    public void delete(Long fileId, Long userId) {
        File file = findFileOrElseThrow(fileId);
        User user = userService.findUserOrElseThrow(userId);

        if(!file.getUser().equals(user)){
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
            // TODO: 4/8/24 에러메세지 통일시키기 -> 문장을 이넘으로
        }

        fileUploadService.delete(file.getKeyName());
        fileRepository.delete(file);
    }

    public FileReadResponseDto getPresignedURL(Long fileId, Long userId) {
        File file = findFileOrElseThrow(fileId);
        User user = userService.findUserOrElseThrow(userId);

        if(!file.getUser().equals(user)){
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
        }

        String preSignedUrl = fileUploadService.getPresignedURL(file.getKeyName());
        return new FileReadResponseDto(preSignedUrl);
    }

    private File findFileOrElseThrow(Long fileId){
        return fileRepository.findById(fileId).orElseThrow(
            ()-> new NullPointerException("없는 이미지 입니다.")
        );
    }

    private String createKeyName(String objectName) {
        String uuid = UUID.randomUUID().toString();
        String extension = objectName.replaceAll(" ", ""); // 공백제거 옥 장 판
        return extension + uuid;
    }

}
