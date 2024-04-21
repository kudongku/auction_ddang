package com.ip.ddangddangddang.domain.user.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignupRequestDto {

    @NotBlank(message = "이메일은 필수로 입력해야 합니다.")
    @Email(message = "이메일 형식에 맞지 않습니다")
    private String email;

    @NotBlank(message = "닉네임은 필수로 입력해야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String passwordConfirm;

    @NotBlank
    private String address;

}
