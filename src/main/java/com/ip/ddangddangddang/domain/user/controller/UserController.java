package com.ip.ddangddangddang.domain.user.controller;


import com.ip.ddangddangddang.domain.common.dto.Response;
import com.ip.ddangddangddang.domain.user.dto.request.UserLocationRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserSignupRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserUpdateRequestDto;
import com.ip.ddangddangddang.domain.user.dto.response.UserResponse;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public Response<UserResponse> getUser(@PathVariable Long userId) {
        return Response.ok(userService.getUser(userId));
    }

    @PostMapping("/signup") @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        userService.signup(requestDto);
    }

    @PatchMapping
    public void updateUser(
        @Valid @RequestBody UserUpdateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.updateUser(requestDto, userDetails.getUserId());
    }

    @DeleteMapping
    public void deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUserId());
    }

    @PatchMapping("/location")
    public void updateLocation(
        @RequestBody UserLocationRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.updateLocation(userDetails.getUserId(), requestDto);
    }
}
