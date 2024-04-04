package com.ip.ddangddangddang.domain.user.repository;

import com.ip.ddangddangddang.domain.user.entity.User;

public interface UserRepository {

    void save(User user);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    User findById(Long id);

    void delete(User user);

}
