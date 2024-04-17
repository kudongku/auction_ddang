package com.ip.ddangddangddang.domain.bid.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BidRequestDto {

    @Positive(message = "음수나, 공백이 올 수 없습니다.")
    private Long price;

}
