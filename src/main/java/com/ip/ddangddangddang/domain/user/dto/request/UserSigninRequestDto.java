package com.ip.ddangddangddang.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSigninRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
