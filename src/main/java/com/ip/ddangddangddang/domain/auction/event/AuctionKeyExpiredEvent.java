package com.ip.ddangddangddang.domain.auction.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuctionKeyExpiredEvent {
    private Long auctionId;
}
