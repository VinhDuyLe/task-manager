package dev.scu.taskmanager.controller;


import dev.scu.taskmanager.model.Notification;
import dev.scu.taskmanager.model.User;
import dev.scu.taskmanager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;


@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addNotificationsToModel(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            List<Notification> notifications = notificationService.getUnreadNotifications(user);
            model.addAttribute("notifications", notifications);
        }
    }
}

