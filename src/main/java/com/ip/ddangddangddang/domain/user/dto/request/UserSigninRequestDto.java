package com.ip.ddangddangddang.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserSigninRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
