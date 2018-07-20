package com.example.demo.controller;

import com.example.demo.data.RegisterUserHelper;
import com.example.demo.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/group")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    //打开注册页面
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerGroupHelper", new RegisterUserHelper());
        return "groupRegister";
    }
}
