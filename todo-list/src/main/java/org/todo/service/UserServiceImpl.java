package org.todo.service;

import lombok.RequiredArgsConstructor;
import org.todo.model.*;
import org.todo.perository.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final CatalogueRepository catalogueRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
    ////////////////////////////USER/ROLES////////////////////////////////////////
    @Override
    public boolean saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null ) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean saveRole(Role role) {
        if (roleRepository.findByName(role.getName()) != null) {
            return false;
        }
        roleRepository.save(role);
        return true;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    //////////////////////////CATEGORIES//////////////////////////////////////
    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElse(new Category());
    }
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    //////////////////////////CATALOGUES//////////////////////////////////////
    @Override
    public void saveCatalogue(Catalogue catalogue) {
        catalogueRepository.save(catalogue);
    }

    @Override
    public Catalogue getCatalogue(Long id){
        return catalogueRepository.findById(id).orElse(new Catalogue());
    }

    @Override
    public void deleteCatalogue(Long id) {
        catalogueRepository.deleteById(id);
    }
    //////////////////////////TASKS///////////////////////////////////////
    @Override
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElse(new Task());
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}