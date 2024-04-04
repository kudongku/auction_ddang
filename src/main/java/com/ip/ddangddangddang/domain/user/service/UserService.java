package com.ip.ddangddangddang.domain.user.service;

import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.dto.request.UserSignupRequestDto;
import com.ip.ddangddangddang.domain.user.dto.request.UserUpdateRequestDto;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.repository.UserRepository;
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
        // 비밀번호 일치 확인
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String email = requestDto.getEmail();
        validateEmail(email);
        String nickname = requestDto.getNickname();
        validateNickname(nickname);

        String password = passwordEncoder.encode(requestDto.getPassword());

        userRepository.save(new User(email, nickname, password, townService.getTown(1L)));
    }

    @Transactional
    public void updateUser(UserUpdateRequestDto requestDto, User user) {
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String nickname = requestDto.getNickname();
        validateNickname(nickname);
        String password = passwordEncoder.encode(requestDto.getPassword());

        User saveUser = userRepository.findById(user.getId());
        saveUser.updateUser(nickname, password);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("동일한 이메일이 존재합니다.");
        }
    }

    private void validateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("동일한 닉네임이 존재합니다.");
        }
    }

}
