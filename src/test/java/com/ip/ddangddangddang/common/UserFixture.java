package com.ip.ddangddangddang.common;

import com.ip.ddangddangddang.domain.user.dto.request.UserLocationRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserSignupRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserUpdateRequestDto;
import com.ip.ddangddangddang.domain.user.dto.response.UserResponse;
import com.ip.ddangddangddang.domain.user.entity.User;

public interface UserFixture extends TownFixture{

    Long TEST_USER1_ID = 1L;
    Long TEST_USER2_ID = 2L;
    Long TEST_USER3_ID = 3L;
    Long TEST_USER4_ID = 4L;

    Long TEST_ANOTHER_USER1_ID = 5L; // 원래 통일하게 맞춰야하는데 바꿔야할 게 많아서 추가 유저 아이디입니다.
    Long TEST_NON_EXISTENT_USER_ID = 10L; // 존재하지 않는 유저 아이디

    String TEST_USER_EMAIL = "testUserEmail@test.com";
    String TEST_USER_NICKNAME = "testUserNickname";
    String TEST_USER_NICKNAME_UPDATED = "testUserNicknameUpdated";
    String TEST_BUYER_USER_NICKNAME = "testBuyerNickname";
    String TEST_USER_PASSWORD = "testPassword";
    String TEST_USER_PASSWORD_UPDATED = "testPasswordUpdated";
    String TEST_USER_DIFFERENT_PASSWORD = "testDifferentPassword";
    String TEST_USER_ENCRYPTED_PASSWORD = "testEncryptedPassword";

    User TEST_USER1 = User.builder()
        .id(TEST_USER1_ID)
        .email(TEST_USER_EMAIL)
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_ENCRYPTED_PASSWORD)
        .town(TEST_TOWN1)
        .build();

    User TEST_USER2 = User.builder()
        .id(TEST_USER2_ID)
        .email(TEST_USER_EMAIL)
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_ENCRYPTED_PASSWORD)
        .town(TEST_TOWN2)
        .build();

    User TEST_USER3 = User.builder()
        .id(TEST_USER3_ID)
        .email(TEST_USER_EMAIL)
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_ENCRYPTED_PASSWORD)
        .town(TEST_ANOTHER_TOWN1)
        .build();

    User TEST_USER4 = User.builder()
        .id(TEST_USER4_ID)
        .email(TEST_USER_EMAIL)
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_ENCRYPTED_PASSWORD)
        .town(TEST_ANOTHER_TOWN2)
        .build();

    User TEST_BUYER_USER1 = User.builder()
        .id(TEST_ANOTHER_USER1_ID)
        .email(TEST_USER_EMAIL)
        .nickname(TEST_BUYER_USER_NICKNAME)
        .password(TEST_USER_ENCRYPTED_PASSWORD)
        .town(TEST_TOWN1)
        .build();

    UserSignupRequestDto TEST_SIGN_UP_REQUEST_DTO = UserSignupRequestDto.builder()
        .email(TEST_USER_EMAIL)
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_PASSWORD)
        .passwordConfirm(TEST_USER_PASSWORD)
        .address(TEST_TOWN1_NAME)
        .build();

    UserSignupRequestDto TEST_SIGN_UP_REQUEST_DTO_PASSWORD_FAIL = UserSignupRequestDto.builder()
        .email(TEST_USER_EMAIL)
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_PASSWORD)
        .passwordConfirm(TEST_USER_DIFFERENT_PASSWORD)
        .address(TEST_TOWN1_NAME)
        .build();

    UserUpdateRequestDto TEST_UPDATE_REQUEST_DTO = UserUpdateRequestDto.builder()
        .nickname(TEST_USER_NICKNAME_UPDATED)
        .password(TEST_USER_PASSWORD_UPDATED)
        .passwordConfirm(TEST_USER_PASSWORD_UPDATED)
        .build();

    UserUpdateRequestDto TEST_UPDATE_REQUEST_DTO_NICKNAME_FAIL = UserUpdateRequestDto.builder()
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_PASSWORD_UPDATED)
        .passwordConfirm(TEST_USER_PASSWORD_UPDATED)
        .build();

    UserUpdateRequestDto TEST_UPDATE_REQUEST_DTO_PASSWORD_FAIL = UserUpdateRequestDto.builder()
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_PASSWORD_UPDATED)
        .passwordConfirm(TEST_USER_PASSWORD)
        .build();

    UserLocationRequestDto TEST_LOCATION_REQUEST_DTO = UserLocationRequestDto.builder()
        .address(TEST_ANOTHER_TOWN1_NAME)
        .build();

    UserLocationRequestDto TEST_LOCATION_REQUEST_DTO_FAIL = UserLocationRequestDto.builder()
        .address(TEST_NON_EXISTENT_TOWN_NAME)
        .build();

    UserResponse TEST_USER_RESPONSE = UserResponse.builder()
        .townId(TEST_USER1_ID)
        .townName(TEST_TOWN1_NAME)
        .build();

}
