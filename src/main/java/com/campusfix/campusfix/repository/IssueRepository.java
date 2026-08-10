package com.campusfix.campusfix.repository;

import com.campusfix.campusfix.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssueRepository
        extends JpaRepository<Issue, Long> {

    Optional<Issue> findByTitleAndLocationAndStatus(
            String title,
            String location,
            String status
    );
}