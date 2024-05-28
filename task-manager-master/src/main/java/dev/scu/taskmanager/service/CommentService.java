package dev.scu.taskmanager.service;

import dev.scu.taskmanager.model.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);
    List<Comment> findCommentsByTaskId(Long taskId);
}

