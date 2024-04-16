package com.ip.ddangddangddang.domain.bid.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BidRequestDto {

    @Positive(message = "음수나, 공백이 올 수 없습니다.")
    private Long price;

}
