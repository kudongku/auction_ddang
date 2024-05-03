package com.ip.ddangddangddang.domain.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuctionListResponseDto implements Serializable {

    private Long auctionId;
    private String title;
    private StatusEnum status;
    private String nickname;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime finishedAt;
    private String filePath;
    private Long price;

    public AuctionListResponseDto(
        Long auctionId,
        String title,
        StatusEnum status,
        String nickname,
        LocalDateTime finishedAt,
        String filePath,
        Long price
    ) {
        this.auctionId = auctionId;
        this.title = title;
        this.status = status;
        this.nickname = nickname;
        this.finishedAt = finishedAt;
        this.filePath = filePath;
        this.price = price;
    }
}
