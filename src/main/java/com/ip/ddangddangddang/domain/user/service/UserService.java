package com.ip.ddangddangddang.domain.user.service;

import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.dto.request.UserLocationRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserSignupRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserUpdateRequestDto;
import com.ip.ddangddangddang.domain.user.dto.response.UserResponse;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.repository.UserRepository;
import com.ip.ddangddangddang.global.exception.custom.CustomTownException;
import com.ip.ddangddangddang.global.exception.custom.CustomUserException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TownService townService;

    @Transactional
    public void signup(UserSignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        existsEmail(email);
        String nickname = requestDto.getNickname();
        existsNickname(nickname);

        validatePassword(requestDto.getPassword(), requestDto.getPasswordConfirm());

        String password = passwordEncoder.encode(requestDto.getPassword());

        userRepository.save(new User(
            email,
            nickname,
            password,
            townService.findTownByNameOrElseThrow(requestDto.getAddress())));
    }

    @Transactional
    public void updateUser(UserUpdateRequestDto requestDto, Long userId) {
        String nickname = requestDto.getNickname();
        existsNickname(nickname);

        validatePassword(requestDto.getPassword(), requestDto.getPasswordConfirm());

        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = getUserByIdOrElseThrow(userId);
        user.updateUser(nickname, password);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(getUserByIdOrElseThrow(userId));
    }

    @Transactional
    public void updateLocation(Long userId, UserLocationRequestDto requestDto) {
        User user = getUserByIdOrElseThrow(userId);
        user.updateLocation(townService.findTownByName(requestDto.getAddress()).orElseThrow(
            () -> new CustomTownException("해당 동네가 없습니다.")
        ));
    }

    // 굳이 하고 싶을 때 이런 식으로 하나 더 만들어도 된다.
    public User getUserByIdOrElseThrow(Long userId) {
        return getUserById(userId).orElseThrow(
            () -> new CustomUserException("회원이 존재하지 않습니다."));
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    private void existsEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomUserException("동일한 이메일이 존재합니다.");
        }
    }

    private void existsNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomUserException("동일한 닉네임이 존재합니다.");
        }
    }

    private void validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new CustomUserException("비밀번호가 일치하지 않습니다.");
        }
    }

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomUserException("회원이 존재하지 않습니다."));
        return new UserResponse(
            user.getTown().getId(),
            user.getTown().getName());

    }
}
