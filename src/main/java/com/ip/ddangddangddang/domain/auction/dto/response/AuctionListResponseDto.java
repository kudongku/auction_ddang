package com.ip.ddangddangddang.domain.auction.dto.response;

import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuctionListResponseDto {

    private Long auctionId;
    private String title;
    private StatusEnum status;
    private LocalDateTime finishedAt;
    private String filePath;

    public AuctionListResponseDto(
        Long auctionId,
        String title,
        StatusEnum status,
        LocalDateTime finishedAt,
        String filePath
    ) {
        this.auctionId = auctionId;
        this.title = title;
        this.status = status;
        this.finishedAt = finishedAt;
        this.filePath = filePath;
    }
}
