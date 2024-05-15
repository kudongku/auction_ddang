package com.ip.ddangddangddang.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ip.ddangddangddang.common.UserFixture;
import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.repository.UserRepository;
import com.ip.ddangddangddang.global.exception.customedExceptions.CustomUserException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest implements UserFixture {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TownService townService;

    @Nested
    @DisplayName("회원가입 요청 테스트")
    class signup {

        @Test
        void 성공() {
            // given
            // when & then
            assertDoesNotThrow(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO));
            verify(userRepository, times(1))
                .save(any(User.class));
        }

        @Test
        void 실패_중복된_이메일() {
            // given
            given(userRepository.existsByEmail(TEST_USER1.getEmail()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO))
                .isInstanceOf(CustomUserException.class)
                .hasMessageContaining("동일한 이메일이 존재합니다.");
        }

        @Test
        void 실패_중복된_닉네임() {
            // given
            given(userRepository.existsByNickname(TEST_USER1.getNickname()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO))
                .isInstanceOf(CustomUserException.class)
                .hasMessageContaining("동일한 닉네임이 존재합니다.");
        }

        @Test
        void 실패_비밀번호_불일치() {
            // given
            // when & then
            assertThatThrownBy(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO_PASSWORD_FAIL))
                .isInstanceOf(CustomUserException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
        }

    }

    @Nested
    @DisplayName("유저 정보 수정 테스트")
    class updateUser {

        @Test
        void 성공() {
            // given
            given(userRepository.findById(TEST_USER2_ID)).willReturn(Optional.of(TEST_USER2));

            // when & then
            var request = TEST_UPDATE_REQUEST_DTO;
            assertDoesNotThrow(() -> userService.updateUser(request, TEST_USER2_ID));

            assertThat(TEST_USER2.getNickname()).isEqualTo(request.getNickname());
            assertThat(TEST_USER2.getPassword()).isEqualTo(
                passwordEncoder.encode(request.getPassword()));
        }

        @Test
        void 실패_중복된_닉네임() {
            // given
            given(userRepository.existsByNickname(TEST_USER1.getNickname()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(
                () -> userService.updateUser(TEST_UPDATE_REQUEST_DTO_NICKNAME_FAIL, TEST_USER1_ID))
                .isInstanceOf(CustomUserException.class)
                .hasMessageContaining("동일한 닉네임이 존재합니다.");
        }

        @Test
        void 실패_비밀번호_불일치() {
            // given
            // when & then
            assertThatThrownBy(
                () -> userService.updateUser(TEST_UPDATE_REQUEST_DTO_PASSWORD_FAIL, TEST_USER1_ID))
                .isInstanceOf(CustomUserException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
        }

    }

    @Nested
    @DisplayName("회원탈퇴 테스트")
    class deleteUser {

        @Test
        void 성공() {
            // given
            given(userRepository.findById(TEST_USER1_ID)).willReturn(Optional.of(TEST_USER1));

            // when & then
            assertDoesNotThrow(() -> userService.deleteUser(TEST_USER1_ID));
            verify(userRepository, times(1)).findById(TEST_USER1_ID);
            verify(userRepository, times(1)).delete(TEST_USER1);
        }

        @Test
        void 실패_존재하지_않는_유저() {
            // given
            // when & then
            assertThatThrownBy(() -> userService.deleteUser(TEST_NON_EXISTENT_USER_ID))
                .isInstanceOf(CustomUserException.class)
                .hasMessageContaining("회원이 존재하지 않습니다.");
            verify(userRepository, never()).delete(any());
        }

    }

    @Nested
    @DisplayName("유저 위치 수정 테스트")
    class updateLocation {

        @Test
        void 성공() {
            // given
            given(userRepository.findById(TEST_USER2_ID)).willReturn(Optional.of(TEST_USER2));
            given(townService.findTownByNameOrElseThrow(TEST_ANOTHER_TOWN1_NAME)).willReturn(TEST_ANOTHER_TOWN1);

            // when & then
            var request = TEST_LOCATION_REQUEST_DTO;
            assertDoesNotThrow(() -> userService.updateLocation(TEST_USER2_ID, request));

            assertThat(TEST_USER2.getTown().getName()).isEqualTo(request.getAddress());
        }

        @Test
        void 실패_존재하지_않는_유저() {
            // given
            // when & then
            assertThatThrownBy(() -> userService.updateLocation(TEST_NON_EXISTENT_USER_ID, TEST_LOCATION_REQUEST_DTO))
                .isInstanceOf(CustomUserException.class)
                .hasMessageContaining("회원이 존재하지 않습니다.");
        }

    }

    @Nested
    @DisplayName("유저의 동네정보 획득 테스트")
    class getUser {

        @Test
        void 성공() {
            // given
            given(userRepository.findById(TEST_USER1_ID)).willReturn(Optional.of(TEST_USER1));

            // when
            var result = userService.getUser(TEST_USER1_ID);

            // then
            assertThat(result.getTownId()).isEqualTo(TEST_TOWN1_ID);
            assertThat(result.getTownName()).isEqualTo(TEST_TOWN1_NAME);
        }

        @Test
        void 실패_존재하지_않는_유저() {
            // given
            // when & then
            assertThatThrownBy(() -> userService.getUser(TEST_NON_EXISTENT_USER_ID))
                .isInstanceOf(CustomUserException.class)
                .hasMessageContaining("회원이 존재하지 않습니다.");
        }

    }

}
