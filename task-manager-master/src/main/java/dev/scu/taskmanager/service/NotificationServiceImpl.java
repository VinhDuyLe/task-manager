package dev.scu.taskmanager.service;


import dev.scu.taskmanager.model.Notification;
import dev.scu.taskmanager.model.User;
import dev.scu.taskmanager.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByUserAndReadStatusFalse(user);
    }

    @Override
    public void markAsRead(Notification notification) {
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
}


