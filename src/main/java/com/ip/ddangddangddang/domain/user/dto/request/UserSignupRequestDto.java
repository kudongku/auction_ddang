package com.ip.ddangddangddang.domain.user.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserSignupRequestDto {

    @NotBlank(message = "이메일은 필수로 입력해야 합니다.")
    @Email(message = "이메일 형식에 맞지 않습니다")
    private String email;

    @NotBlank(message = "닉네임은 필수로 입력해야 합니다.")
    @Pattern(
        regexp = "^(?!\\d+$)[a-zA-Z가-힣\\d]{2,10}$",
        message = "닉네임은 2~10자로 구성되어야 하며, 숫자로만 구성될 수 없습니다."
    )
    private String nickname;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\W)(?=.*\\d).{8,15}$",
        message = "비밀번호는 최소 8자 이상, 15자 이하로 알파벳과 특수문자, 숫자로 구성되어야 합니다."
    )
    // 알파벳 대문자, 소문자, 숫자, 특수문자 각각 1개 이상 필요
    private String password;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String passwordConfirm;

}
