package com.ip.ddangddangddang.domain.auction.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuctionRequestDto {

    @NotBlank(message = "제목이 없습니다")
    private String title;

    @NotBlank(message = "내용이 없습니다")
    private String content;

    @NotBlank(message = "물건 이름이 없습니다")
    private Long fileId;
}
