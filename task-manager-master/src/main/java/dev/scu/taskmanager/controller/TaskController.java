package dev.scu.taskmanager.controller;

import dev.scu.taskmanager.model.Comment;
import dev.scu.taskmanager.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import dev.scu.taskmanager.model.Task;
import dev.scu.taskmanager.model.User;
import dev.scu.taskmanager.service.TaskService;
import dev.scu.taskmanager.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;
    private CommentService commentService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, CommentService commentService) {
        this.taskService = taskService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/tasks")
    public String listTasks(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", false);
        return "views/tasks";
    }

    @GetMapping("/tasks/in-progress")
    public String listTasksInProgress(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", true);
        return "views/tasks";
    }

    private void prepareTasksListModel(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    @GetMapping("/task/create")
    public String showEmptyTaskForm(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        Task task = new Task();
        task.setCreatorName(user.getName());
        if (request.isUserInRole("ROLE_USER")) {
            task.setOwner(user);
        }
        model.addAttribute("task", task);
        return "forms/task-new";
    }

    @PostMapping("/task/create")
    public String createTask(@Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/task-new";
        }
        taskService.createTask(task);

        return "redirect:/tasks";
    }

    @GetMapping("/task/edit/{id}")
    public String showFilledTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "forms/task-edit";
    }

    @PostMapping("/task/edit/{id}")
    public String updateTask(@Valid Task task, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/task-edit";
        }
        taskService.updateTask(id, task);

        return "redirect:/tasks";
    }

    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/mark-done/{id}")
    public String setTaskCompleted(@PathVariable Long id) {
        taskService.setTaskCompleted(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/unmark-done/{id}")
    public String setTaskNotCompleted(@PathVariable Long id) {
        taskService.setTaskNotCompleted(id);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{taskId}/comments")
    public String addComment(@PathVariable Long taskId, @RequestParam String content, Principal principal, Model model) {
        Task task = taskService.getTaskById(taskId);
        Comment comment = new Comment();
        comment.setContent(content);
        LocalDateTime currentTime = LocalDateTime.now();

        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        comment.setUser(user);
        comment.setDate(currentTime);
        comment.setTask(task);
        commentService.addComment(comment);

        model.addAttribute("task", task);
        return "redirect:/tasks";  // Redirect back to the task details view
    }










}
