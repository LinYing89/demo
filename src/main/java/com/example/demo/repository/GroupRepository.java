package com.example.demo.repository;

import com.example.demo.data.DevGroup;
import com.example.demo.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<DevGroup, Long> {

    List<DevGroup> findByUserId(long userId);
}
