package com.ip.ddangddangddang.domain.user.service;

import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.dto.request.UserLocationRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserSignupRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserUpdateRequestDto;
import com.ip.ddangddangddang.domain.user.dto.response.UserResponse;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.repository.UserRepository;
import com.ip.ddangddangddang.global.exception.customedExceptions.CustomUserException;
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
        String nickname = requestDto.getNickname();
        validateEmail(email);
        validateNickname(nickname);
        validatePassword(requestDto.getPassword(), requestDto.getPasswordConfirm());
        String password = passwordEncoder.encode(requestDto.getPassword());

        userRepository.save(
            new User(
                email,
                nickname,
                password,
                townService.findTownByNameOrElseThrow(requestDto.getAddress())
            )
        );
    }

    @Transactional
    public void updateUser(UserUpdateRequestDto requestDto, Long userId) {
        String nickname = requestDto.getNickname();
        validateNickname(nickname);
        validatePassword(requestDto.getPassword(), requestDto.getPasswordConfirm());
        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = findUserById(userId);
        user.updateUser(nickname, password);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(findUserById(userId));
    }

    @Transactional
    public void updateLocation(Long userId, UserLocationRequestDto requestDto) {
        User user = findUserById(userId);
        user.updateLocation(townService.findTownByNameOrElseThrow(requestDto.getAddress()));
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new CustomUserException("회원이 존재하지 않습니다.")
        );
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomUserException("동일한 이메일이 존재합니다.");
        }
    }

    private void validateNickname(String nickname) {
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
        User user = userRepository.findById(userId).orElseThrow(
            () -> new CustomUserException("회원이 존재하지 않습니다.")
        );

        return new UserResponse(
            user.getTown().getId(),
            user.getTown().getName()
        );
    }
}
