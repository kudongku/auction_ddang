package com.ip.ddangddangddang.domain.auction.dto.response;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AuctionResponseDto {

    private Long id;

    private Long townId;

    private String title;

    private String content;

    private StatusEnum statusEnum;

    private LocalDateTime finishedAt;

    private Long sellerId;

    public AuctionResponseDto(Auction auction) {
        this.id = auction.getId();
        this.townId = auction.getTownId();
        this.title = auction.getTitle();
        this.content = auction.getContent();
        this.statusEnum = auction.getStatusEnum();
        this.finishedAt = auction.getFinishedAt();
        this.sellerId = auction.getUser().getId();
    }

}