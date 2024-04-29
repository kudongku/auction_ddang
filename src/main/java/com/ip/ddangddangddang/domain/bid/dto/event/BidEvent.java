package com.ip.ddangddangddang.domain.bid.dto.event;

import lombok.Getter;

@Getter
public class BidEvent {

    private final Long bidId;
    private final Long auctionId;
    private final Long userId;
    private final Long price;

    public BidEvent(Long bidId, Long auctionId, Long userId, Long price) {
        this.bidId = bidId;
        this.auctionId = auctionId;
        this.userId = userId;
        this.price = price;
    }
}
