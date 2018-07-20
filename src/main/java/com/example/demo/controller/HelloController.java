package com.example.demo.controller;

import com.example.demo.data.RegisterUserHelper;
import com.example.demo.data.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(value = "/")
public class HelloController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/")
    public String hello(Model model, @CookieValue(value="userId",required=false) String userId){
        if(null == userId || userId.isEmpty()){
            return gotoLogin(model);
        }
        Optional<User> optionalUser = userRepository.findById(Long.parseLong(userId));
        if(!optionalUser.isPresent()){
            return gotoLogin(model);
        }

        RegisterUserHelper registerUserHelper = new RegisterUserHelper();
        registerUserHelper.setName(optionalUser.get().getName());
        model.addAttribute("registerUserHelper", registerUserHelper);
        return "/hello";
    }

    private String gotoLogin(Model model){
        RegisterUserHelper registerUserHelper = new RegisterUserHelper();
        model.addAttribute("registerUserHelper", registerUserHelper);
        return "/login";
    }
}
