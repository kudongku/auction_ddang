package com.ip.ddangddangddang.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
        import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSigninRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
