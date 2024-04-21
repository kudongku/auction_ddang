package com.ip.ddangddangddang.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateRequestDto {

    @NotBlank(message = "닉네임은 필수로 입력해야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String passwordConfirm;

}
