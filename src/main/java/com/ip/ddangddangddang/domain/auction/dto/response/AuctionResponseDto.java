package com.ip.ddangddangddang.domain.auction.dto.response;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionResponseDto {

    private String preSignedURL;

    public AuctionResponseDto(Auction auction, String preSignedURL) {
        this.preSignedURL = preSignedURL;
    }
}
