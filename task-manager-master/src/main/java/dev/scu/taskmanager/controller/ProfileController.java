package dev.scu.taskmanager.controller;

import dev.scu.taskmanager.model.Notification;
import dev.scu.taskmanager.service.NotificationService;
import dev.scu.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import dev.scu.taskmanager.model.User;
import dev.scu.taskmanager.service.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class ProfileController {

    private UserService userService;
    private TaskService taskService;
    private NotificationService notificationService;

    @Autowired
    public ProfileController(UserService userService, TaskService taskService,NotificationService notificationService) {
        this.userService = userService;
        this.taskService = taskService;
        this.notificationService=notificationService;
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        List<Notification> notifications = Collections.emptyList();
        if (user != null) {
            notifications = notificationService.getUnreadNotifications(user);
        }
        model.addAttribute("notifications", notifications);

        model.addAttribute("user", user);
        model.addAttribute("tasksOwned", taskService.findByOwnerOrderByDateDesc(user));
        return "views/profile";
    }

    @GetMapping("/profile/mark-done/{taskId}")
    public String setTaskCompleted(@PathVariable Long taskId) {
        taskService.setTaskCompleted(taskId);
        return "redirect:/profile";
    }

    @GetMapping("/profile/unmark-done/{taskId}")
    public String setTaskNotCompleted(@PathVariable Long taskId) {
        taskService.setTaskNotCompleted(taskId);
        return "redirect:/profile";
    }



}
