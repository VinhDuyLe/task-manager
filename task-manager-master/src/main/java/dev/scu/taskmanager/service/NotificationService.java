package dev.scu.taskmanager.service;

import dev.scu.taskmanager.model.Notification;
import dev.scu.taskmanager.model.User;

import java.util.List;




public interface NotificationService {
    void saveNotification(Notification notification);
    List<Notification> getUnreadNotifications(User user);
    void markAsRead(Notification notification);
}

