package org.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.todo.model.User;
import org.todo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping
    public String addUser(@ModelAttribute("userForm") User userForm,
                          Model model) {
        userForm.setCategoryToViewId(-1L);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("d.M.y H:m:ss");
        userForm.setRegistrationDate(now.format(df));
        userForm.setIsAdmin(false);
        userForm.setIsActive(true);

        if (userForm.getUsername().length() < 4 || userForm.getPassword().length() < 4) {
            model.addAttribute("lengthError",
                    "Минимальная длина логина и пароля 4 символа");
            return "registration";
        }
        if (userForm.getUsername().length() > 32 || userForm.getPassword().length() > 32) {
            model.addAttribute("lengthError",
                    "Максимальная длина логина и пароля 32 символа");
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            model.addAttribute("confirmPasswordError",
                    "Введенные пароли не совпадают");
            return "registration";
        }
        if (!userService.saveUser(userForm)) {
            model.addAttribute("usernameError",
                    "Пользователь с таким логином уже существует");
            return "registration";
        }
        userService.addRoleToUser(userForm.getUsername(), "ROLE_USER");
        return "redirect:/";
    }
}