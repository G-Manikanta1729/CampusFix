package com.campusfix.campusfix.controller;

import com.campusfix.campusfix.dto.AuthenticationRequest;
import com.campusfix.campusfix.dto.AuthenticationResponse;
import com.campusfix.campusfix.dto.IssueCreateResponse;
import com.campusfix.campusfix.dto.IssueRequest;
import com.campusfix.campusfix.dto.NotificationResponse;
import com.campusfix.campusfix.dto.RegisterRequest;
import com.campusfix.campusfix.model.Issue;
import com.campusfix.campusfix.repository.UserRepository;
import com.campusfix.campusfix.service.AuthenticationService;
import com.campusfix.campusfix.service.IssueService;
import com.campusfix.campusfix.service.NotificationService;
import com.campusfix.campusfix.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PageController {

    private final IssueService issueService;
    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;

    public PageController(
            IssueService issueService,
            NotificationService notificationService,
            AuthenticationService authenticationService,
            UserService userService,
            UserRepository userRepository) {

        this.issueService = issueService;
        this.notificationService = notificationService;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // =========================================================
    // HOME
    // =========================================================

    @GetMapping("/")
    public String homePage(Model model) {

        model.addAttribute(
                "message",
                "Welcome to CampusFix"
        );

        return "index";
    }

    // =========================================================
    // REGISTRATION
    // =========================================================

    @GetMapping("/register")
    public String registerPage(Model model) {

        RegisterRequest request =
                new RegisterRequest();

        request.setRole("STUDENT");

        model.addAttribute(
                "registerRequest",
                request
        );

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute RegisterRequest request,
            RedirectAttributes redirectAttributes) {

        try {

            // Public registration creates students only
            request.setRole("STUDENT");

            userService.register(request);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Registration successful. Please login."
            );

            return "redirect:/login";

        } catch (RuntimeException exception) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage()
            );

            return "redirect:/register";
        }
    }

    // =========================================================
    // LOGIN
    // =========================================================

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute AuthenticationRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        try {

            AuthenticationResponse authenticationResponse =
                    authenticationService.authenticate(request);

            ResponseCookie cookie =
                    ResponseCookie.from(
                                    "CAMPUSFIX_JWT",
                                    authenticationResponse.getToken()
                            )
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(60 * 60)
                            .sameSite("Lax")
                            .build();

            response.setHeader(
                    "Set-Cookie",
                    cookie.toString()
            );

            String role = userRepository
                    .findByUsername(request.getUsername())
                    .orElseThrow()
                    .getRole();

            if ("ADMIN".equalsIgnoreCase(role)) {

                return "redirect:/admin";
            }

            return "redirect:/student";

        } catch (AuthenticationException exception) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Invalid username or password."
            );

            return "redirect:/login";
        }
    }

    // =========================================================
    // STUDENT PAGE
    // =========================================================

    @GetMapping("/student")
    public String studentPage(
            Model model,
            Authentication authentication) {

        List<Issue> issues =
                issueService.getIssues();

        model.addAttribute(
                "issues",
                issues
        );

        List<NotificationResponse> notifications =
                notificationService
                        .getStudentNotifications(authentication.getName());

        model.addAttribute(
                "notifications",
                notifications
        );

        model.addAttribute(
                "username",
                authentication.getName()
        );

        return "student";
    }

    // =========================================================
    // STUDENT - CREATE ISSUE
    // =========================================================

    @PostMapping("/student/issues")
    public String createIssueFromPage(
            @ModelAttribute IssueRequest request,
            RedirectAttributes redirectAttributes) {

        IssueCreateResponse response =
                issueService.createIssue(request);

        redirectAttributes.addFlashAttribute(
                "message",
                response.getMessage()
        );

        return "redirect:/student";
    }

    // =========================================================
    // STUDENT - MARK NOTIFICATION AS READ
    // =========================================================

    @PostMapping("/student/notifications/read")
    public String markStudentNotificationAsRead(
            @RequestParam Long notificationId,
            RedirectAttributes redirectAttributes) {

        notificationService.markAsRead(
                notificationId
        );

        redirectAttributes.addFlashAttribute(
                "message",
                "Notification marked as read."
        );

        return "redirect:/student";
    }

    // =========================================================
    // ADMIN PAGE
    // =========================================================

    @GetMapping("/admin")
    public String adminPage(Model model) {

        List<Issue> issues =
                issueService.getIssues();

        List<NotificationResponse> notifications =
                notificationService
                        .getAdminNotifications();

        model.addAttribute(
                "issues",
                issues
        );

        model.addAttribute(
                "notifications",
                notifications
        );

        return "admin";
    }

    // =========================================================
    // ADMIN - UPDATE ISSUE STATUS
    // =========================================================

    @PostMapping("/admin/issues/{id}/status")
    public String updateIssueStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        issueService.updateIssueStatus(
                id,
                status
        );

        redirectAttributes.addFlashAttribute(
                "message",
                "Issue status updated successfully."
        );

        return "redirect:/admin";
    }

    // =========================================================
    // ADMIN - DELETE ISSUE
    // =========================================================

    @PostMapping("/admin/issues/{id}/delete")
    public String deleteIssueFromPage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        issueService.deleteIssue(id);

        redirectAttributes.addFlashAttribute(
                "message",
                "Issue deleted successfully."
        );

        return "redirect:/admin";
    }

    // =========================================================
    // ADMIN - MARK NOTIFICATION AS READ
    // =========================================================

    @PostMapping("/admin/notifications/read")
    public String markAdminNotificationAsRead(
            @RequestParam Long notificationId,
            RedirectAttributes redirectAttributes) {

        notificationService.markAsRead(
                notificationId
        );

        redirectAttributes.addFlashAttribute(
                "message",
                "Notification marked as read."
        );

        return "redirect:/admin";
    }
}