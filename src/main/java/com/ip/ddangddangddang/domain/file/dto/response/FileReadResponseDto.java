package com.ip.ddangddangddang.domain.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileReadResponseDto {

    private Long fileId;

    private String preSignedUrl;

}
