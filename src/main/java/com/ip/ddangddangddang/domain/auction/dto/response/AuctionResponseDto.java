package com.ip.ddangddangddang.domain.auction.dto.response;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AuctionResponseDto {

    private Long id;

    private String townName;

    private String title;

    private String content;

    private Long price;

    private StatusEnum statusEnum;

    private LocalDateTime finishedAt;

    private String sellerNickname;

    private String buyerNickname;

    public AuctionResponseDto(Auction auction, String townName, String buyerNickname) {
        this.id = auction.getId();
        this.townName = townName;
        this.title = auction.getTitle();
        this.content = auction.getContent();
        this.price = auction.getPrice();
        this.statusEnum = auction.getStatusEnum();
        this.finishedAt = auction.getFinishedAt();
        this.sellerNickname = auction.getUser().getNickname();
        this.buyerNickname = buyerNickname;
    }

}
