package com.ip.ddangddangddang.domain.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionResponseDto implements Serializable {

    private Long id;
    private String townName;
    private String title;
    private String content;
    private Long price;
    private StatusEnum statusEnum;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime finishedAt;
    private String sellerNickname;
    private String buyerNickname;
    private String filePath;

    public AuctionResponseDto(Auction auction, String townName, String buyerNickname, String filePath) {
        this.id = auction.getId();
        this.townName = townName;
        this.title = auction.getTitle();
        this.content = auction.getContent();
        this.price = auction.getPrice();
        this.statusEnum = auction.getStatusEnum();
        this.finishedAt = auction.getFinishedAt();
        this.sellerNickname = auction.getUser().getNickname();
        this.buyerNickname = buyerNickname;
        this.filePath = filePath;
    }

}
