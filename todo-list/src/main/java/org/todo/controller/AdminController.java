package org.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.todo.model.User;
import org.todo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping
    public String getUsers(Model model, Principal principal) {

        model.addAttribute("activeUser", userService.getUser(principal.getName()));
        model.addAttribute("allUsers", userService.getUsers());
        return "admin";
    }

    @PostMapping("/remove/user/{id}")
    private String deleteUser(@PathVariable("id") Long id,
                              Principal principal) {
        User user = userService.getUser(principal.getName());
        if (user.getIsAdmin()) {
            userService.deleteUser(id);
        }
        return "redirect:/admin";
    }

    @PostMapping("/user/{id}")
    public String banUser(@PathVariable("id") Long id,
                        @ModelAttribute("isBanned") String updatedBanStatus,
                        @ModelAttribute("isAdmin") String updatedAdminStatus,
                        Principal principal) {
        User user = userService.getUser(principal.getName());
        if (user.getIsAdmin()) {
            User userToUpdate = userService.getUser(id);
            userToUpdate.setIsActive(!"yes".equals(updatedBanStatus));
            userToUpdate.setIsAdmin("yes".equals(updatedAdminStatus));
            userService.saveUser(userToUpdate);

            if ("yes".equals(updatedAdminStatus)) {
                userService.addRoleToUser(userToUpdate.getUsername(), "ROLE_ADMIN");
            } else {
                userService.removeRoleFromUser(userToUpdate.getUsername(), "ROLE_ADMIN");
            }
        }
        return "redirect:/admin";
    }
}