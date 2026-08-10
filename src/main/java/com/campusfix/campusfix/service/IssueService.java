package com.campusfix.campusfix.service;

import com.campusfix.campusfix.model.Issue;
import com.campusfix.campusfix.repository.IssueRepository;
import org.springframework.stereotype.Service;
import com.campusfix.campusfix.exception.IssueNotFoundException;
import com.campusfix.campusfix.dto.IssueRequest;
import com.campusfix.campusfix.dto.IssueResponse;
import com.campusfix.campusfix.dto.IssueCreateResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final NotificationService notificationService;

    public IssueService(
            IssueRepository issueRepository,
            NotificationService notificationService) {

        this.issueRepository = issueRepository;
        this.notificationService = notificationService;
    }

    public List<Issue> getIssues() {
        return issueRepository.findAll();
    }

    public IssueCreateResponse createIssue(IssueRequest request) {

        Optional<Issue> existingIssue =
                issueRepository.findByTitleAndLocationAndStatus(
                        request.getTitle(),
                        request.getLocation(),
                        "OPEN"
                );

        if (existingIssue.isPresent()) {

            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            String username = authentication.getName();

            notificationService
                    .createDuplicateNotification(username);

            return new IssueCreateResponse(
                    "This issue has already been reported.",
                    null,
                    existingIssue.get().getId()
            );
        }

        Issue issue = new Issue();

        issue.setTitle(request.getTitle());
        issue.setLocation(request.getLocation());
        issue.setStatus("OPEN");

        Issue savedIssue = issueRepository.save(issue);

        IssueResponse response = new IssueResponse(
                savedIssue.getId(),
                savedIssue.getTitle(),
                savedIssue.getLocation(),
                savedIssue.getStatus()
        );

        return new IssueCreateResponse(
                "Issue created successfully",
                response,
                null
        );
    }

    public IssueResponse updateIssue(
            Long id,
            IssueRequest request) {

        Issue existingIssue = issueRepository.findById(id)
                .orElseThrow(() ->
                        new IssueNotFoundException(
                                "Issue not found with id: " + id));

        existingIssue.setTitle(request.getTitle());
        existingIssue.setLocation(request.getLocation());

        Issue savedIssue = issueRepository.save(existingIssue);

        return new IssueResponse(
                savedIssue.getId(),
                savedIssue.getTitle(),
                savedIssue.getLocation(),
                savedIssue.getStatus()
        );
    }

    public Issue updateIssueStatus(
            Long id,
            String status) {

        Issue existingIssue =
                issueRepository.findById(id)
                        .orElseThrow(() ->
                                new IssueNotFoundException(
                                        "Issue not found with id: " + id));

        existingIssue.setStatus(status);

        return issueRepository.save(existingIssue);
    }

    public Issue getIssueById(Long id) {

        return issueRepository.findById(id)
                .orElseThrow(() ->
                        new IssueNotFoundException(
                                "Issue not found with id: " + id));
    }

    public void deleteIssue(Long id) {
        issueRepository.deleteById(id);
    }
}