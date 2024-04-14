package com.ip.ddangddangddang.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long townId;
    private final String townName;

    public UserResponse(Long id, String name) {
        this.townId = id;
        this.townName = name;
    }
}
