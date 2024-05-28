package dev.scu.taskmanager.repository;



import dev.scu.taskmanager.model.Notification;
import dev.scu.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndReadStatusFalse(User user);
}

