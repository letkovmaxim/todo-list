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
public class MainController {
    private final UserService userService;
    private long categoryToViewId = -1L;

    @GetMapping("/main")
    public String main(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        model.addAttribute("activeUser", user);
        model.addAttribute("categoryToView", userService.getCategory(categoryToViewId));
        return "main";
    }

    @PostMapping("/main/add/category")
    public String addCategory(@ModelAttribute Category category,
                              Principal principal) {
        User user = userService.getUser(principal.getName());
        Category categoryToAdd = new Category();
        categoryToAdd.setName(category.getName());
        categoryToAdd.setUser(user);
        userService.saveCategory(categoryToAdd);
        return "redirect:/main";
    }

    @PostMapping("/main/category/{id}/add/catalogue")
    public String addCatalogueToCategory(@PathVariable("id") Long id,
                                         @ModelAttribute Catalogue catalogue,
                                         Principal principal) {
        Catalogue catalogueToAdd = new Catalogue();
        if (userService.getCategory(id).getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            catalogueToAdd.setName(catalogue.getName());
            catalogueToAdd.setCategory(userService.getCategory(id));
            userService.saveCatalogue(catalogueToAdd);
            categoryToViewId = id;
        }
        return "redirect:/main";
    }

    @PostMapping("/main/catalogue/{id}/add/task")
    public String addTaskToCatalogue(@PathVariable("id") Long id,
                                     @ModelAttribute Task task,
                                     Principal principal) {
        Task taskToAdd = new Task();
        if (userService.getCatalogue(id).getCategory().getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            taskToAdd.setName(task.getName());
            taskToAdd.setCatalogue(userService.getCatalogue(id));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/y H:m:ss");
            taskToAdd.setCreationDate(now.format(df));
            userService.saveTask(taskToAdd);
        }
        return "redirect:/main";
    }

    @PostMapping("/main/remove/category/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Principal principal) {
        if (userService.getCategory(id).getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            userService.deleteCategory(id);
        }
        return "redirect:/main";
    }

    @PostMapping("/main/remove/catalogue/{id}")
    public String deleteCatalogue(@PathVariable("id") Long id, Principal principal) {
        if (userService.getCatalogue(id).getCategory().getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            userService.deleteCatalogue(id);
        }
        return "redirect:/main";
    }

    @PostMapping("/main/remove/task/{id}")
    public String deleteTask(@PathVariable("id") Long id, Principal principal) {
//        if (userService.getTask(id).getCatalogue().getCategory().getUser().getId().longValue()
//                == userService.getUser(principal.getName()).getId().longValue()) {
//            userService.deleteTask(id);
//        }
        userService.deleteTask(id);
        return "redirect:/main";
    }

    @PostMapping("/main/view/category/{id}")
    public String viewCategory(@PathVariable long id, Principal principal) {
        if (userService.getCategory(id).getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            categoryToViewId = id;
        }
        return "redirect:/main";
    }

    @PostMapping("/main/rename/category/{id}")
    public String renameCategory(@PathVariable("id") Long id,
                                 @ModelAttribute("name") String name,
                                 Principal principal) {
        Category categoryToRename = userService.getCategory(id);
        if (categoryToRename.getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            categoryToRename.setName(name);
            userService.saveCategory(categoryToRename);
        }
        return "redirect:/main";
    }

    @PostMapping("/main/rename/catalogue/{id}")
    public String renameCatalogue(@PathVariable("id") Long id,
                                  @ModelAttribute("name") String name,
                                  Principal principal) {
        Catalogue catalogueToRename = userService.getCatalogue(id);
        if (catalogueToRename.getCategory().getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            catalogueToRename.setName(name);
            userService.saveCatalogue(catalogueToRename);
        }
        return "redirect:/main";
    }

    @PostMapping("/main/rename/task/{id}")
    public String renameTask(@PathVariable("id") Long id,
                             @ModelAttribute("name") String name,
                             Principal principal) {
        Task taskToRename = userService.getTask(id);
        if (taskToRename.getCatalogue().getCategory().getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            taskToRename.setName(name);
            userService.saveTask(taskToRename);
        }
        return "redirect:/main";
    }

    @PostMapping("/main/task/{id}/set/status")
    public String setDoneTask(@PathVariable("id") Long id,
                              @ModelAttribute("doneStatus") String doneStatus,
                              @ModelAttribute("importantStatus") String importantStatus,
                              Principal principal) {
        Task taskToSetStatus = userService.getTask(id);
        if (taskToSetStatus.getCatalogue().getCategory().getUser().getId().longValue()
                == userService.getUser(principal.getName()).getId().longValue()) {
            taskToSetStatus.setIsDone("on".equals(doneStatus));
            taskToSetStatus.setIsImportant("on".equals(importantStatus));
            userService.saveTask(taskToSetStatus);
        }
        return "redirect:/main";
    }

}