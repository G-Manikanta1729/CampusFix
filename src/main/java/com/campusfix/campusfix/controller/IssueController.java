package com.campusfix.campusfix.controller;

import com.campusfix.campusfix.model.Issue;
import com.campusfix.campusfix.service.IssueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.campusfix.campusfix.dto.IssueRequest;
import com.campusfix.campusfix.dto.IssueResponse;
import com.campusfix.campusfix.dto.IssueCreateResponse;

import jakarta.validation.Valid;


import java.util.List;

@RestController
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping("/issues")
    public List<Issue> getIssues() {
        return issueService.getIssues();
    }

    @GetMapping("/issues/{id}")
    public Issue getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }

    @PostMapping("/issues")
    public IssueCreateResponse createIssue(
            @RequestBody @Valid IssueRequest request) {

        return issueService.createIssue(request);
    }

    @PutMapping("/issues/{id}")
    public IssueResponse updateIssue(
            @PathVariable Long id,
            @RequestBody @Valid IssueRequest request) {

        return issueService.updateIssue(id, request);
    }

    @DeleteMapping("/issues/{id}")
    public void deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
    }
}