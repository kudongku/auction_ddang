package com.ip.ddangddangddang.domain.auction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomSlice {

    private Slice<AuctionListResponseDto> slice;

}
