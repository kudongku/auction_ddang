package com.ip.ddangddangddang.domain.user.service;

import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.dto.request.UserLocationRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserSignupRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserUpdateRequestDto;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.repository.UserRepository;
import com.ip.ddangddangddang.global.exception.custom.CustomUserException;
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

        User user = findUserOrElseThrow(userId);
        user.updateUser(nickname, password);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(findUserOrElseThrow(userId));
    }

    @Transactional
    public void updateLocation(Long userId, UserLocationRequestDto requestDto) {
        User user = findUserOrElseThrow(userId);
        user.updateLocation(townService.findTownByNameOrElseThrow(requestDto.getAddress()));
    }

    public User findUserOrElseThrow(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new CustomUserException("회원이 존재하지 않습니다.")
        );
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

}
