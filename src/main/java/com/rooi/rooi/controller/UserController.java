package com.rooi.rooi.controller;

import com.rooi.rooi.dto.LoginRequestDto;
import com.rooi.rooi.dto.SignupRequestDto;
import com.rooi.rooi.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/profile")
    public String profilePage() {
        return "profile";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupRequestDto requestDto) { //Dto -> 로 생성자 생성

        userService.signup(requestDto);

        return "redirect:/user/login-page";

    }
//    필터에서 처리
//    @PostMapping("/user/login")
//    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
//        try {
//            userService.login(requestDto, res);
//        } catch (Exception e) {
//            return "redirect:/api/user/login-page?error";
//        }
//
//        return "redirect:/";
//
//    }
}