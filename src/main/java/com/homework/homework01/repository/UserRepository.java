package com.homework.homework01.repository;

import com.homework.homework01.dto.LoginRequestDto;
import com.homework.homework01.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
