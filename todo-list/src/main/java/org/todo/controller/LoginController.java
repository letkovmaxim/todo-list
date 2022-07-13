package org.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.todo.model.User;
import org.todo.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping("/fail")
    public String loginFail(@ModelAttribute("username") String username,
                            Model model) {
        User user = userService.getUser(username);

        if (user != null) {
            if (!user.getIsActive()) {
                model.addAttribute("loginError",
                        "Ваш аккаунт был заблокирован");
            } else {
                model.addAttribute("loginError",
                        "Неверный пароль");
            }
        } else {
            model.addAttribute("loginError",
                    "Неверный логин");
        }
        return "login";
    }
}