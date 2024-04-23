package com.ip.ddangddangddang.domain.file.controller;

import com.ip.ddangddangddang.domain.common.dto.Response;
import com.ip.ddangddangddang.domain.file.dto.request.FileCreateRequestDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileCreateResponseDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileReadResponseDto;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/v1/auctions/files")
@RestController
public class FileController {

    private final FileService fileService;

    @PostMapping
    public Response<FileCreateResponseDto> uploadImage(
        @RequestPart("auctionImage") MultipartFile auctionImage,
        @Valid @RequestPart("requestDto") FileCreateRequestDto imageRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FileCreateResponseDto fileCreateResponseDto = fileService.upload(
            auctionImage,
            imageRequestDto.getImageName(),
            userDetails.getUserId()
        );
        return Response.ok(fileCreateResponseDto);
    }

    @GetMapping("/{fileId}")
    public Response<FileReadResponseDto> getPreSignedUrl(
        @PathVariable Long fileId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FileReadResponseDto fileReadResponseDto = fileService.getPresignedURL(
            fileId,
            userDetails.getUserId()
        );
        return Response.ok(fileReadResponseDto);
    }

}
