package com.example.demo.controller;

import com.example.demo.data.Config;
import com.example.demo.data.DevGroup;
import com.example.demo.data.RegisterUserHelper;
import com.example.demo.data.User;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/group")
@SessionAttributes("user")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Config config;

    //打开注册页面
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerGroupHelper", new RegisterUserHelper());
        return "group/groupRegister";
    }

    //注册页面提交
    @PostMapping("/register")
    public String registerSubmit(Model model, @ModelAttribute RegisterUserHelper groupHelper) {
        if(groupHelper.getName().isEmpty()){
            groupHelper.setUserNameError("组名为空");
            return "groupRegister";
        }
        DevGroup group = new DevGroup();
        group.setName(groupHelper.getName());
        User user = (User) model.asMap().get("user");
        user.addGroup(group);
        groupRepository.save(group);
        return "group/groupList";
    }

    //打开组列表页面
    @GetMapping("/list/{userId}")
    public String showGroupList(@PathVariable long userId, Model model) {
        if(!model.containsAttribute("user")){
            Optional<User> optionalUser = userRepository.findById(userId);
            optionalUser.ifPresent(user -> model.addAttribute("user", user));
        }
        return "group/groupList";
    }

    //打开组页面
    @GetMapping("/{groupId}")
    public String showGroup(@PathVariable long groupId, Model model) {
        User user = (User) model.asMap().get("user");
        DevGroup group = user.findDevGroupById(groupId);
        model.addAttribute("group", group);
        model.addAttribute("config", config);
        return "group/group";
    }

    //打开组页面
    @GetMapping("/{groupId}")
    public String history(@PathVariable long groupId, Model model) {
        User user = (User) model.asMap().get("user");
        DevGroup group = user.findDevGroupById(groupId);
        model.addAttribute("group", group);
        model.addAttribute("config", config);
        return "group/group";
    }
}
