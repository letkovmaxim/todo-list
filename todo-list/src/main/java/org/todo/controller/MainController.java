package org.todo.controller;

import lombok.RequiredArgsConstructor;
import org.todo.model.Category;
import org.todo.model.Task;
import org.todo.model.Catalogue;
import org.todo.model.User;
import org.todo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final UserService userService;

    @GetMapping
    public String main(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());

        //Авторизированный пользователь и категория, которую он просматривает
        model.addAttribute("activeUser", user);
        model.addAttribute("categoryToView", userService.getCategory(user.getCategoryToViewId()));
        return "main";
    }

    @PostMapping("/add/category")
    public String addCategory(@ModelAttribute Category category,
                              Principal principal) {
        User user = userService.getUser(principal.getName());

        Category categoryToAdd = new Category();
        categoryToAdd.setName(category.getName());
        categoryToAdd.setUser(user);

        userService.saveCategory(categoryToAdd);
        return "redirect:/main";
    }

    @PostMapping("/category/{id}/add/catalogue")
    public String addCatalogueToCategory(@PathVariable("id") Long id,
                                         @ModelAttribute Catalogue catalogue,
                                         Principal principal) {
        User user = userService.getUser(principal.getName());
        Category categoryToAddIn = userService.getCategory(id);
        Catalogue catalogueToAdd = new Catalogue();

        //Проверка пользователя
        if (isCategoryBelongsToUser(categoryToAddIn, user)) {

            catalogueToAdd.setName(catalogue.getName());
            catalogueToAdd.setCategory(categoryToAddIn);
            catalogueToAdd.setUser(user);

            userService.saveCatalogue(catalogueToAdd);
        }
        return "redirect:/main";
    }

    @PostMapping("/catalogue/{id}/add/task")
    public String addTaskToCatalogue(@PathVariable("id") Long id,
                                     @ModelAttribute Task task,
                                     Principal principal) {
        User user = userService.getUser(principal.getName());
        Catalogue catalogueToAddIn = userService.getCatalogue(id);
        Task taskToAdd = new Task();

        //Проверка пользователя
        if (isCatalogueBelongsToUser(catalogueToAddIn, user)) {

            taskToAdd.setName(task.getName());
            taskToAdd.setCatalogue(catalogueToAddIn);
            taskToAdd.setUser(user);

            //Время создания
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/y H:m:ss");
            taskToAdd.setCreationDate(now.format(df));

            userService.saveTask(taskToAdd);
        }
        return "redirect:/main";
    }

    @PostMapping("/remove/category/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getUser(principal.getName());
        Category category = userService.getCategory(id);

        //Проверка пользователя
        if (isCategoryBelongsToUser(category, user)) {
            userService.deleteCategory(id);
        }
        return "redirect:/main";
    }

    @PostMapping("/remove/catalogue/{id}")
    public String deleteCatalogue(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getUser(principal.getName());
        Catalogue catalogue = userService.getCatalogue(id);

        //Проверка пользователя
        if (isCatalogueBelongsToUser(catalogue, user)) {
            userService.deleteCatalogue(id);
        }
        return "redirect:/main";
    }

    @PostMapping("/remove/task/{id}")
    public String deleteTask(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getUser(principal.getName());
        Task task = userService.getTask(id);

        //Проверка пользователя
        if (isTaskBelongsToUser(task, user)) {
            userService.deleteTask(id);
        }
        return "redirect:/main";
    }

    @PostMapping("/view/category/{id}")
    public String viewCategory(@PathVariable long id, Principal principal) {
        User user = userService.getUser(principal.getName());
        Category category = userService.getCategory(id);

        //Проверка пользователя
        if (isCategoryBelongsToUser(category, user)) {

            user.setCategoryToViewId(id);
            userService.saveUser(user);
        }
        return "redirect:/main";
    }

    @PostMapping("/rename/category/{id}")
    public String renameCategory(@PathVariable("id") Long id,
                                 @ModelAttribute("name") String name,
                                 Principal principal) {
        User user = userService.getUser(principal.getName());
        Category categoryToRename = userService.getCategory(id);

        //Проверка пользователя
        if (isCategoryBelongsToUser(categoryToRename, user)) {

            categoryToRename.setName(name);
            userService.saveCategory(categoryToRename);
        }
        return "redirect:/main";
    }

    @PostMapping("/rename/catalogue/{id}")
    public String renameCatalogue(@PathVariable("id") Long id,
                                  @ModelAttribute("name") String name,
                                  Principal principal) {
        User user = userService.getUser(principal.getName());
        Catalogue catalogueToRename = userService.getCatalogue(id);

        //Проверка пользователя
        if (isCatalogueBelongsToUser(catalogueToRename, user)) {

            catalogueToRename.setName(name);
            userService.saveCatalogue(catalogueToRename);
        }
        return "redirect:/main";
    }

    @PostMapping("/rename/task/{id}")
    public String renameTask(@PathVariable("id") Long id,
                             @ModelAttribute("name") String name,
                             Principal principal) {
        User user = userService.getUser(principal.getName());
        Task taskToRename = userService.getTask(id);

        //Проверка пользователя
        if (isTaskBelongsToUser(taskToRename, user)) {

            taskToRename.setName(name);
            userService.saveTask(taskToRename);
        }
        return "redirect:/main";
    }

    @PostMapping("/task/{id}/set/status")
    public String setTaskStatus(@PathVariable("id") Long id,
                                @ModelAttribute("doneStatus") String doneStatus,
                                @ModelAttribute("importantStatus") String importantStatus,
                                @ModelAttribute("favoriteStatus") String favoriteStatus,
                                Principal principal) {
        User user = userService.getUser(principal.getName());
        Task taskToSetStatus = userService.getTask(id);

        //Проверка пользователя
        if (isTaskBelongsToUser(taskToSetStatus, user)) {

            //Установка статусов
            taskToSetStatus.setIsDone("on".equals(doneStatus));
            taskToSetStatus.setIsImportant("on".equals(importantStatus));
            taskToSetStatus.setIsFavorite("on".equals(favoriteStatus));

            userService.saveTask(taskToSetStatus);
        }
        return "redirect:/main";
    }

    private boolean isCategoryBelongsToUser(Category category, User user) {
        return category.getUser().getId().longValue() == user.getId().longValue();
    }

    private boolean isCatalogueBelongsToUser(Catalogue catalogue, User user) {
        return catalogue.getUser().getId().longValue() == user.getId().longValue();
    }

    private boolean isTaskBelongsToUser(Task task, User user) {
        return task.getUser().getId().longValue() == user.getId().longValue();
    }
}