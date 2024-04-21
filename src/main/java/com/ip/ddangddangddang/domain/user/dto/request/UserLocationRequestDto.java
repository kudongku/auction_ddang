package com.ip.ddangddangddang.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLocationRequestDto {

    @NotBlank
    private String address;

}
