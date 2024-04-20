package com.ip.ddangddangddang.domain.auction.dto.response;

import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AuctionUpdateResponseDto {

    private Long auctionId;
    private Long townId;
    private String title;
    private String content;
    private Long price;
    private Long buyerId;
    private StatusEnum statusEnum;
    private LocalDateTime finishedAt;

    public AuctionUpdateResponseDto(Long auctionId, Long townId, String title, String content,
        Long price, Long buyerId, StatusEnum statusEnum, LocalDateTime finishedAt) {
        this.auctionId = auctionId;
        this.townId = townId;
        this.title = title;
        this.content = content;
        this.price = price;
        this.buyerId = buyerId;
        this.statusEnum = statusEnum;
        this.finishedAt = finishedAt;
    }
}
