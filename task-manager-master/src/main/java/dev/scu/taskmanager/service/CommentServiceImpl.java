package dev.scu.taskmanager.service;

import dev.scu.taskmanager.model.Notification;
import dev.scu.taskmanager.model.Task;
import dev.scu.taskmanager.model.User;
import dev.scu.taskmanager.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.scu.taskmanager.model.Comment;
import dev.scu.taskmanager.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, NotificationRepository notificationRepository) {
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void addComment(Comment comment) {

        commentRepository.save(comment);

        Task task = comment.getTask();
        User owner = task.getOwner();

        if (owner != null) {
            Notification notification = new Notification();
            notification.setUser(owner);
            notification.setMessage("A new comment has been made on your task: " + task.getName());
            notification.setId(task.getId());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setReadStatus(false);
            notificationRepository.save(notification);
        }



    }

    @Override
    public List<Comment> findCommentsByTaskId(Long taskId) {
        return commentRepository.findAll().stream()
                .filter(comment -> comment.getTask().getId().equals(taskId))
                .collect(Collectors.toList());
    }
}

