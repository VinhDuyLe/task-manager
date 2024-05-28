package dev.scu.taskmanager.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import dev.scu.taskmanager.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

