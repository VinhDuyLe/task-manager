package dev.scu.taskmanager.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import dev.scu.taskmanager.model.Role;
import dev.scu.taskmanager.model.User;
import dev.scu.taskmanager.model.Task;
import dev.scu.taskmanager.service.RoleService;
import dev.scu.taskmanager.service.TaskService;
import dev.scu.taskmanager.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;
    private final TaskService taskService;
    private final RoleService roleService;
    private final Logger logger = LoggerFactory.getLogger(InitialDataLoader.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Value("${default.admin.mail}")
    private String defaultAdminMail;
    @Value("${default.admin.name}")
    private String defaultAdminName;
    @Value("${default.admin.password}")
    private String defaultAdminPassword;
    @Value("${default.admin.image}")
    private String defaultAdminImage;

    @Autowired
    public InitialDataLoader(UserService userService, TaskService taskService, RoleService roleService) {
        this.userService = userService;
        this.taskService = taskService;
        this.roleService = roleService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Create roles
        roleService.createRole(new Role("ADMIN"));
        roleService.createRole(new Role("USER"));
        roleService.findAll().stream().map(role -> "Saved role: " + role.getRole()).forEach(logger::info);

        // Create users
        createUserData();

        // Create tasks
        createTaskData();

        // Log created tasks
        taskService.findAll().stream().map(t -> "Saved task: '" + t.getName() + "' for owner: " + getOwnerNameOrNoOwner(t)).forEach(logger::info);
    }

    private void createUserData() {
        // Admin user
        User admin = new User(
                defaultAdminMail,
                defaultAdminName,
                defaultAdminPassword,
                defaultAdminImage);
        userService.createUser(admin);
        userService.changeRoleToAdmin(admin);

        // Manager user
        User manager = new User(
                "manager@mail.com",
                "Manager",
                "password123",
                "images/manager.jpg");
        userService.createUser(manager);
        userService.changeRoleToAdmin(manager);

        // Other users
        userService.createUser(new User(
                "john.doe@mail.com",
                "John Doe",
                "password123",
                "images/johndoe.jpg"));

        userService.createUser(new User(
                "jane.doe@mail.com",
                "Jane Doe",
                "password123",
                "images/janedoe.jpg"));

        userService.createUser(new User(
                "alice.smith@mail.com",
                "Alice Smith",
                "password123",
                "images/alice.jpg"));

        userService.createUser(new User(
                "bob.jones@mail.com",
                "Bob Jones",
                "password123",
                "images/bob.jpg"));

        userService.createUser(new User(
                "charlie.brown@mail.com",
                "Charlie Brown",
                "password123",
                "images/charlie.jpg"));

        userService.findAll().stream()
                .map(u -> "Saved user: " + u.getName())
                .forEach(logger::info);
    }

    private void createTaskData() {
        LocalDate today = LocalDate.now();

        // Task 1
        taskService.createTask(new Task(
                "Initial client meeting",
                "Setup first meeting with client to discuss project requirements.",
                today.minusDays(40),
                true,
                userService.getUserByEmail("john.doe@mail.com").getName(),
                userService.getUserByEmail("john.doe@mail.com")
        ));

        // Task 2
        taskService.createTask(new Task(
                "Define project scope",
                "Define the project scope and gather all necessary requirements.",
                today.minusDays(30),
                true,
                userService.getUserByEmail("jane.doe@mail.com").getName(),
                userService.getUserByEmail("jane.doe@mail.com")
        ));

        // Task 3
        taskService.createTask(new Task(
                "Conduct industry research",
                "Research client's industry to understand market trends and competitors.",
                today.minusDays(20),
                true,
                userService.getUserByEmail("alice.smith@mail.com").getName(),
                userService.getUserByEmail("alice.smith@mail.com")
        ));

        // Task 4
        taskService.createTask(new Task(
                "Estimate project cost",
                "Estimate the total cost for the project, including development and design.",
                today.minusDays(10),
                true,
                userService.getUserByEmail("bob.jones@mail.com").getName(),
                userService.getUserByEmail("bob.jones@mail.com")
        ));

        // Task 5
        taskService.createTask(new Task(
                "Secure project hosting",
                "Get a quotation for hosting services and secure the necessary resources.",
                today.minusDays(5),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("bob.jones@mail.com")
        ));

        // Task 6
        taskService.createTask(new Task(
                "Quality check content",
                "Review and quality check all content to be used in the project.",
                today.minusDays(2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("john.doe@mail.com")
        ));

        // Task 7
        taskService.createTask(new Task(
                "Design Contact Us page",
                "Design and implement the Contact Us page with accurate details.",
                today.minusDays(1),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("john.doe@mail.com")
        ));

        // Task 8
        taskService.createTask(new Task(
                "Proofread web copy",
                "Proofread all web copy for grammar and spelling errors.",
                today,
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("jane.doe@mail.com")
        ));

        // Task 9
        taskService.createTask(new Task(
                "Validate media assets",
                "Ensure all images and videos are properly placed and formatted.",
                today.plusDays(1),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("jane.doe@mail.com")
        ));

        // Task 10
        taskService.createTask(new Task(
                "Test internal links",
                "Test all internal links to ensure they work correctly.",
                today.plusDays(2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("bob.jones@mail.com")
        ));

        // Task 11
        taskService.createTask(new Task(
                "Test forms functionality",
                "Verify that all forms are submitting data correctly and thank-you messages are displayed.",
                today.plusDays(3),
                false,
                userService.getUserByEmail("bob.jones@mail.com").getName(),
                userService.getUserByEmail("bob.jones@mail.com")
        ));

        // Task 12
        taskService.createTask(new Task(
                "Verify external links",
                "Ensure all external links are working and open in a new tab.",
                today.plusDays(4),
                false,
                userService.getUserByEmail("jane.doe@mail.com").getName(),
                userService.getUserByEmail("jane.doe@mail.com")
        ));

        // Task 13
        taskService.createTask(new Task(
                "Check 404 page",
                "Test the 404 page and ensure redirects are working correctly.",
                today.plusDays(5),
                false,
                userService.getUserByEmail("alice.smith@mail.com").getName(),
                userService.getUserByEmail("alice.smith@mail.com")
        ));

        // Task 14
        taskService.createTask(new Task(
                "Ensure mobile compatibility",
                "Ensure the website is mobile-friendly and meets the required standards.",
                today.plusDays(6),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com")
        ));

        // Task 15
        taskService.createTask(new Task(
                "Test on various devices",
                "Test the website on different devices and emulators.",
                today.plusDays(8),
                false,
                userService.getUserByEmail("manager@mail.com").getName()
        ));

        // Task 16
        taskService.createTask(new Task(
                "Review meta data",
                "Ensure all pages have unique titles, descriptions, and keywords.",
                today.plusDays(10),
                false,
                userService.getUserByEmail("manager@mail.com").getName()
        ));

        // Task 17
        taskService.createTask(new Task(
                "Verify URL structure",
                "Ensure all URLs are consistent with the site's architecture.",
                today.plusDays(12),
                false,
                userService.getUserByEmail("manager@mail.com").getName()
        ));

        // Task 18
        taskService.createTask(new Task(
                "Optimize assets",
                "Minify JS and CSS files, optimize images, and enable gzip compression.",
                today.plusDays(14),
                false,
                userService.getUserByEmail("manager@mail.com").getName()
        ));

        // Task 19
        taskService.createTask(new Task(
                "Setup social media profiles",
                "Register and set up social media profiles with appropriate branding.",
                today.plusDays(16),
                false,
                userService.getUserByEmail("manager@mail.com").getName()
        ));

        // Task 20
        taskService.createTask(new Task(
                "Deliver final site",
                "Send the final site to the client, incorporate feedback, and get sign-off.",
                today.plusDays(18),
                false,
                userService.getUserByEmail("manager@mail.com").getName()
        ));
    }

    private String getOwnerNameOrNoOwner(Task task) {
        return task.getOwner() == null ? "no owner" : task.getOwner().getName();
    }
}
