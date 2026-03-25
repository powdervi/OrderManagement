package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
    User findByUsername(String username);
}
