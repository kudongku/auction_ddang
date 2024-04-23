package com.ip.ddangddangddang.domain.user.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ip.ddangddangddang.common.UserFixture;
import org.junit.jupiter.api.Test;


public class UserTest implements UserFixture {

    @Test
    void 생성자_테스트() {
        // given
        Long userId = TEST_USER1_ID;
        String email = TEST_USER_EMAIL;

        // when
        User user = new User(userId, email);

        // then
        assertNotNull(user); // User 객체가 null이 아닌지 확인
        assertEquals(userId, user.getId()); // userId가 올바르게 설정되었는지 확인
        assertEquals(email, user.getEmail()); // email이 올바르게 설정되었는지 확인
    }

    @Test
    void 기본_생성자_테스트() {
        // given
        User user;

        // when
        user = new User();

        // then
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getNickname());
        assertNull(user.getPassword());
        assertNull(user.getTown());
    }

}
