package com.ip.ddangddangddang.domain.user.controller;


import com.ip.ddangddangddang.domain.user.dto.request.UserLocationRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserSignupRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserUpdateRequestDto;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signup(
        @Valid @RequestBody UserSignupRequestDto requestDto
    ) {
        userService.signup(requestDto);
    }

    @PatchMapping
    public void updateUser(
        @Valid @RequestBody UserUpdateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.updateUser(requestDto, userDetails.getUser().getId());
    }

    @DeleteMapping
    public void deleteUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.deleteUser(userDetails.getUser().getId());
    }

    @PatchMapping("/location")
    public void updateLocation(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody UserLocationRequestDto requestDto
    ) {
        userService.updateLocation(userDetails.getUser().getId(), requestDto);
    }

}
