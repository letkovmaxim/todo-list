package org.todo;

import org.todo.model.Catalogue;
import org.todo.model.Category;
import org.todo.model.Role;
import org.todo.model.User;
import org.todo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_USER"));

            userService.saveUser(new User(null, "admin", "admin", "admin", "admin",
                    new ArrayList<>(), new ArrayList<>(), -1L, "01.01.01", true, true));

            userService.addRoleToUser("admin", "ROLE_USER");
            userService.addRoleToUser("admin", "ROLE_ADMIN");
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
