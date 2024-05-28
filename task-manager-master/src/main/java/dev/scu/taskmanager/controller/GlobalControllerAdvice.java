package dev.scu.taskmanager.controller;


import dev.scu.taskmanager.model.Notification;
import dev.scu.taskmanager.model.User;
import dev.scu.taskmanager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addNotificationsToModel(Model model, @AuthenticationPrincipal User user) {
        List<Notification> notifications = Collections.emptyList();
        if (user != null) {
            notifications = notificationService.getUnreadNotifications(user);
        }
        model.addAttribute("notifications", notifications);
    }
}

