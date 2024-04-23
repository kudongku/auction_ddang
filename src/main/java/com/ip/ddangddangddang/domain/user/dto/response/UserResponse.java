package com.ip.ddangddangddang.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private final Long townId;
    private final String townName;

    public UserResponse(Long id, String name) {
        this.townId = id;
        this.townName = name;
    }
}
