package com.ip.ddangddangddang.domain.auction.dto.response;

import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.auction.model.AuctionModel;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuctionResponseDto {

    private Long id;

    private Long townId;

    private String title;

    private String content;

    private StatusEnum statusEnum;

    private LocalDateTime finishedAt;

    private Long sellerId;

    public AuctionResponseDto(AuctionModel auctionModel) {
        this.id = auctionModel.getId();
        this.townId = auctionModel.getTownId();
        this.title = auctionModel.getTitle();
        this.content = auctionModel.getContent();
        this.statusEnum = auctionModel.getStatusEnum();
        this.finishedAt = auctionModel.getFinishedAt();
        this.sellerId = auctionModel.getUser().getId();
    }

}
