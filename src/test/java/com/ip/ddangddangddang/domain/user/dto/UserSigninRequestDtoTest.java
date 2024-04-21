package com.ip.ddangddangddang.domain.user.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ip.ddangddangddang.common.UserFixture;
import com.ip.ddangddangddang.domain.user.dto.request.UserSigninRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.Test;


public class UserSigninRequestDtoTest implements UserFixture {

    @Test
    void 로그인_요청_성공() {
        // given
        var userSigninRequestDto = UserSigninRequestDto.builder()
            .email(TEST_USER_EMAIL)
            .password(TEST_USER_PASSWORD)
            .build();

        // when
        var violations = validate(userSigninRequestDto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void 로그인_요청_실패() {
        //given
        var userSigninRequestDto = UserSigninRequestDto.builder()
            .email("")
            .password("")
            .build();
        //when
        var violations = validate(userSigninRequestDto);
        //then
        assertThat(violations).hasSize(2);
    }

    private Set<ConstraintViolation<UserSigninRequestDto>> validate(
        UserSigninRequestDto signinRequestDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(signinRequestDto);
    }

}
