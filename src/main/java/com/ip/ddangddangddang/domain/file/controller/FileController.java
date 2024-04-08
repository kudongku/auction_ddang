package com.ip.ddangddangddang.domain.file.controller;

import com.ip.ddangddangddang.domain.file.dto.request.FileCreateRequestDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileCreateResponseDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileReadResponseDto;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/v1/files")
@RestController
public class FileController {

    private final FileService fileService;

    @PostMapping
    public FileCreateResponseDto uploadImage(
        @RequestPart("auctionImage") MultipartFile auctionImage,
        @Valid @RequestPart("requestDto") FileCreateRequestDto imageRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long fileId = fileService.upload(
            auctionImage,
            imageRequestDto.getImageName(),
            userDetails.getUserId()
        );

        return new FileCreateResponseDto(fileId);
    }

    @GetMapping("/{fileId}")
    public FileReadResponseDto getPreSignedUrl(
        @PathVariable Long fileId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return fileService.getPresignedURL(fileId, userDetails.getUserId());
    }

    @DeleteMapping("/{fileId}")
    public void deleteFile(
        @PathVariable Long fileId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        fileService.delete(fileId, userDetails.getUserId());
    }

}
