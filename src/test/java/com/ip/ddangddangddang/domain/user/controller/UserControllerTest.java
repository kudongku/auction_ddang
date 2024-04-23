package com.ip.ddangddangddang.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ip.ddangddangddang.common.ControllerTest;
import com.ip.ddangddangddang.domain.user.dto.request.UserLocationRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserSignupRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserUpdateRequestDto;
import com.ip.ddangddangddang.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTest {

    @MockBean
    private UserService userService;

    @Test
    void 회원가입_테스트() throws Exception {
        // given & when
        var action = mockMvc.perform(post("/v1/users/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(TEST_SIGN_UP_REQUEST_DTO)));

        // then
        action.andExpect(status().isCreated());
        verify(userService, times(1))
            .signup(any(UserSignupRequestDto.class));
    }

    @Test
    void 유저_정보_업데이트_테스트() throws Exception {
        // given & when
        var action = mockMvc.perform(patch("/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(TEST_UPDATE_REQUEST_DTO)));

        // then
        action.andExpect(status().isOk());
        verify(userService, times(1))
            .updateUser(any(UserUpdateRequestDto.class), eq(TEST_USER1_ID));
    }

    @Test
    void 회원탈퇴_테스트() throws Exception {
        // given & when
        var action = mockMvc.perform(delete("/v1/users"));

        // then
        action.andExpect(status().isOk());
        verify(userService, times(1))
            .deleteUser(eq(TEST_USER1_ID));
    }

    @Test
    void 유저_위치변경_테스트() throws Exception {
        // given & when
        var action = mockMvc.perform(patch("/v1/users/location")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(TEST_LOCATION_REQUEST_DTO)));

        // then
        action.andExpect(status().isOk());
        verify(userService, times(1))
            .updateLocation(eq(TEST_USER1_ID), any(UserLocationRequestDto.class));
    }

    @Test
    void 유저_동네정보_가져오기_테스트() throws Exception {
        // given & when
        var action = mockMvc.perform(get("/v1/users/{userId}", TEST_USER1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(TEST_USER_RESPONSE)));

        // then
        action.andExpect(status().isOk());
        verify(userService, times(1))
            .getUser(eq(TEST_USER1_ID));
    }

}
