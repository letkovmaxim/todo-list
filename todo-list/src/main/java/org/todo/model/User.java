package org.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "userSequence")
    @SequenceGenerator(name = "userSequence", initialValue = 1, allocationSize = 1, sequenceName = "USERS_SEQUENCE")
    private Long id;
    private String name;
    private String username;
    private String password;
    private String confirmPassword;
    @ManyToMany(fetch = EAGER)
    private List<Role> roles = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = ALL)
    private List<Category> categories = new ArrayList<>();
    private Long categoryToViewId;
    private String registrationDate;
    private Boolean isAdmin;
    private Boolean isActive;
}
