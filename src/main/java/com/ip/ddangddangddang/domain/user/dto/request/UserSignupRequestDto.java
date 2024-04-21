package com.ip.ddangddangddang.domain.user.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
    private String password;

    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String passwordConfirm;

    @NotBlank
    private String address;

}
