package com.example.demo.repository;

import com.example.demo.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailOrTel(String email, String tel);
}
