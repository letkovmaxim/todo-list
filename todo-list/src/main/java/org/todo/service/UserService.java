package org.todo.service;

import org.todo.model.*;

import java.util.List;

public interface UserService {
    boolean saveUser(User user);
    boolean saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
    void deleteUser(Long id);
    void saveCategory(Category category);
    Category getCategory(Long id);
    void deleteCategory(Long id);
    void saveCatalogue(Catalogue catalogue);

    Catalogue getCatalogue(Long id);
    void deleteCatalogue(Long id);
    void saveTask(Task task);

    Task getTask(Long id);
    void deleteTask(Long id);
}