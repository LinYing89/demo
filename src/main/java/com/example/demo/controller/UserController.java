package com.example.demo.controller;

import com.example.demo.data.RegisterUserHelper;
import com.example.demo.data.Untils;
import com.example.demo.data.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/user")
@SessionAttributes("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //打开注册页面
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerUserHelper", new RegisterUserHelper());
        return "register";
    }

    //提交注册信息
    @PostMapping("/register")
    public String registerSubmit(RedirectAttributes model, @ModelAttribute RegisterUserHelper userHelper) {
        //RegisterUserHelper error = new RegisterUserHelper();
        if(userHelper.getName().isEmpty()){
            userHelper.setUserNameError("用户名为空");
            model.addAttribute("registerUserHelper", userHelper);
            return "register";
        }
        User user = new User();
        user.setName(userHelper.getName());
        boolean isEmail = Untils.isEmail(userHelper.getName());
        if(isEmail){
            user.setEmail(userHelper.getName());
        }else if(Untils.isMobileNumber(userHelper.getName())){
            user.setTel(userHelper.getName());
        }else{
            userHelper.setUserNameError("用户名格式不正确,必须是邮箱或手机号码");
            model.addAttribute("registerUserHelper", userHelper);
            return "register";
        }
        User userDb = userRepository.findByEmailOrTel(user.getEmail(), user.getTel());
        if(null != userDb){
            userHelper.setUserNameError("用户已存在");
            model.addAttribute("registerUserHelper", userHelper);
            return "register";
        }

        if(userHelper.getPassword().isEmpty()){
            userHelper.setPasswordError("密码为空");
            model.addAttribute("registerUserHelper", userHelper);
            return "register";
        }
        if(!userHelper.passwordEnsure()){
            userHelper.setPasswordError("两次输入的密码不一致");
            model.addAttribute("registerUserHelper", userHelper);
            return "register";
        }
        user.setPassword(userHelper.getPassword());
        userRepository.save(user);
        //重定向
        model.addAttribute("userId", user.getId());
        model.addFlashAttribute("user", user);
        return "redirect:/group/list/{userId}";
    }

    //打开登录页面
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("registerUserHelper", new RegisterUserHelper());
        return "login";
    }

    //提交登录信息
    @PostMapping("/login")
    public String loginCheck(HttpServletResponse httpServletResponse, RedirectAttributes model, @ModelAttribute RegisterUserHelper userHelper) {
        User userDb = userRepository.findByEmailOrTel(userHelper.getName(), userHelper.getName());
        if(null == userDb){
            userHelper.setUserNameError("用户不存在");
            return "login";
        }
        //userDb.getDevGroups();
        if(!userDb.getPassword().equals(userHelper.getPassword())){
            userHelper.setPasswordError("密码错误");
            return "login";
        }
        if(userHelper.isAutoLogin()){
            Cookie cookie = new Cookie("userId", String.valueOf(userDb.getId()));
            cookie.setPath("/");//如果需要在跟目录获取,如欢迎页面,必须设置path为"/",否则只能在"/user"路径下获取到,其他路径获取不到
            cookie.setMaxAge(Integer.MAX_VALUE); //设置cookie的过期时间是10s
            httpServletResponse.addCookie(cookie);
        }

        //重定向
        model.addAttribute("userId", userDb.getId());

        model.addFlashAttribute("user", userDb);
        return "redirect:/group/list/{userId}";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse httpServletResponse, Model model){
        Cookie userCookie = new Cookie("userId", "");
        userCookie.setMaxAge(0);
        userCookie.setPath("/");
        httpServletResponse.addCookie(userCookie);
        model.addAttribute("registerUserHelper", new RegisterUserHelper());
        return "login";
    }
}
