package com.example.demo.repository;

import com.example.demo.data.MsgManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MsgManagerRepository extends JpaRepository<MsgManager, Long> {

    public MsgManager findByNum(int num);
}
