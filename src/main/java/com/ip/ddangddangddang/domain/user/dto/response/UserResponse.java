package com.ip.ddangddangddang.domain.user.dto.response;

import com.ip.ddangddangddang.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long townId;
    private String townName;

    public UserResponse(User user) {
        this.townId = user.getTown().getId();
        this.townName = user.getTown().getName();
    }
}
