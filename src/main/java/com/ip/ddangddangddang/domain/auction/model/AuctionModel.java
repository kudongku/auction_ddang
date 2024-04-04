package com.ip.ddangddangddang.domain.auction.model;

import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.result.entity.Result;
import com.ip.ddangddangddang.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionModel {

    private Long id;
    private Long townId;
    private String title;
    private String content;
    private StatusEnum statusEnum;
    private LocalDateTime finishedAt;
    private User user;
    private Result result;

}
