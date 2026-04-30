package com.example.productivity.service;

import com.example.productivity.dto.DeveloperProfileResponse;
import com.example.productivity.dto.MetricsResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    public DeveloperProfileResponse getDeveloperProfile(Long developerId) {
        if (developerId == 1) {
            return new DeveloperProfileResponse(1L, "Alex", "Platform");
        }
        if (developerId == 2) {
            return new DeveloperProfileResponse(2L, "Priya", "Payments");
        }
        return new DeveloperProfileResponse(3L, "Sam", "Core");
    }

    public MetricsResponse getMetricsForDeveloper(Long developerId) {
        return getMetricsForDeveloper(developerId, null, null);
    }

    public MetricsResponse getMetricsForDeveloper(Long developerId, LocalDate from, LocalDate to) {
        List<Issue> issues = getIssues(developerId);
        List<PullRequest> prs = getPullRequests(developerId);
        List<Deployment> deployments = getDeployments(developerId);
        List<Bug> bugs = getBugs(developerId);

        List<Issue> filteredIssues = filterIssuesByDoneDate(issues, from, to);
        List<PullRequest> filteredPrs = filterPrsByCreatedDate(prs, from, to);
        List<Deployment> filteredDeployments = filterDeploymentsByDate(deployments, from, to);
        List<Bug> filteredBugs = filterBugsByDate(bugs, from, to);

        double leadTime = calculateLeadTimeDays(filteredPrs, filteredDeployments);
        double cycleTime = calculateCycleTimeDays(filteredIssues);
        double bugRate = calculateBugRate(filteredBugs, filteredIssues);
        int deploymentFrequency = filteredDeployments.size();
        int prThroughput = (int) filteredPrs.stream().filter(PullRequest::merged).count();

        return new MetricsResponse(
                round2(leadTime),
                round2(cycleTime),
                round2(bugRate),
                deploymentFrequency,
                prThroughput
        );
    }

    private double calculateLeadTimeDays(List<PullRequest> prs, List<Deployment> deployments) {
        double totalDays = 0;
        int count = 0;
        for (PullRequest pr : prs) {
            if (!pr.merged()) {
                continue;
            }
            Deployment deployment = deployments.stream()
                    .filter(d -> d.prId() == pr.id())
                    .findFirst()
                    .orElse(null);
            if (deployment != null) {
                long hours = Duration.between(pr.createdAt(), deployment.deployedAt()).toHours();
                totalDays += hours / 24.0;
                count++;
            }
        }
        return count == 0 ? 0 : totalDays / count;
    }

    private double calculateCycleTimeDays(List<Issue> issues) {
        double totalDays = 0;
        int count = 0;
        for (Issue issue : issues) {
            long hours = Duration.between(issue.inProgressAt(), issue.doneAt()).toHours();
            totalDays += hours / 24.0;
            count++;
        }
        return count == 0 ? 0 : totalDays / count;
    }

    private double calculateBugRate(List<Bug> bugs, List<Issue> issues) {
        if (issues.isEmpty()) {
            return 0;
        }
        return (double) bugs.size() / issues.size();
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private List<Issue> filterIssuesByDoneDate(List<Issue> issues, LocalDate from, LocalDate to) {
        return issues.stream()
                .filter(i -> isInRange(i.doneAt(), from, to))
                .collect(Collectors.toList());
    }

    private List<PullRequest> filterPrsByCreatedDate(List<PullRequest> prs, LocalDate from, LocalDate to) {
        return prs.stream()
                .filter(pr -> isInRange(pr.createdAt(), from, to))
                .collect(Collectors.toList());
    }

    private List<Deployment> filterDeploymentsByDate(List<Deployment> deployments, LocalDate from, LocalDate to) {
        return deployments.stream()
                .filter(d -> isInRange(d.deployedAt(), from, to))
                .collect(Collectors.toList());
    }

    private List<Bug> filterBugsByDate(List<Bug> bugs, LocalDate from, LocalDate to) {
        return bugs.stream()
                .filter(b -> isInRange(b.occurredAt(), from, to))
                .collect(Collectors.toList());
    }

    private boolean isInRange(LocalDateTime value, LocalDate from, LocalDate to) {
        LocalDate date = value.toLocalDate();
        if (from != null && date.isBefore(from)) {
            return false;
        }
        if (to != null && date.isAfter(to)) {
            return false;
        }
        return true;
    }

    private List<Issue> getIssues(Long developerId) {
        if (developerId == 1) {
            return List.of(
                    new Issue(1, dt(2026, 4, 2, 10), dt(2026, 4, 4, 16)),
                    new Issue(2, dt(2026, 4, 6, 11), dt(2026, 4, 8, 15)),
                    new Issue(3, dt(2026, 4, 10, 9), dt(2026, 4, 11, 18)),
                    new Issue(4, dt(2026, 4, 14, 10), dt(2026, 4, 17, 12)),
                    new Issue(5, dt(2026, 4, 20, 11), dt(2026, 4, 23, 17))
            );
        }
        if (developerId == 2) {
            return List.of(
                    new Issue(6, dt(2026, 4, 1, 10), dt(2026, 4, 6, 17)),
                    new Issue(7, dt(2026, 4, 8, 10), dt(2026, 4, 12, 18)),
                    new Issue(8, dt(2026, 4, 13, 11), dt(2026, 4, 18, 16)),
                    new Issue(9, dt(2026, 4, 21, 9), dt(2026, 4, 25, 15))
            );
        }
        return List.of(
                new Issue(10, dt(2026, 4, 2, 10), dt(2026, 4, 5, 16)),
                new Issue(11, dt(2026, 4, 7, 10), dt(2026, 4, 10, 18)),
                new Issue(12, dt(2026, 4, 12, 9), dt(2026, 4, 14, 17)),
                new Issue(13, dt(2026, 4, 16, 11), dt(2026, 4, 19, 16)),
                new Issue(14, dt(2026, 4, 22, 10), dt(2026, 4, 24, 15))
        );
    }

    private List<PullRequest> getPullRequests(Long developerId) {
        if (developerId == 1) {
            return List.of(
                    new PullRequest(100, dt(2026, 4, 3, 9), true),
                    new PullRequest(101, dt(2026, 4, 7, 10), true),
                    new PullRequest(102, dt(2026, 4, 11, 11), true),
                    new PullRequest(103, dt(2026, 4, 15, 12), true),
                    new PullRequest(104, dt(2026, 4, 21, 11), true)
            );
        }
        if (developerId == 2) {
            return List.of(
                    new PullRequest(200, dt(2026, 4, 2, 10), true),
                    new PullRequest(201, dt(2026, 4, 10, 11), true),
                    new PullRequest(202, dt(2026, 4, 14, 12), true)
            );
        }
        return List.of(
                new PullRequest(300, dt(2026, 4, 4, 10), true),
                new PullRequest(301, dt(2026, 4, 9, 11), true),
                new PullRequest(302, dt(2026, 4, 13, 11), true),
                new PullRequest(303, dt(2026, 4, 17, 10), true)
        );
    }

    private List<Deployment> getDeployments(Long developerId) {
        if (developerId == 1) {
            return List.of(
                    new Deployment(100, dt(2026, 4, 5, 10)),
                    new Deployment(101, dt(2026, 4, 9, 15)),
                    new Deployment(102, dt(2026, 4, 13, 17)),
                    new Deployment(103, dt(2026, 4, 18, 11)),
                    new Deployment(104, dt(2026, 4, 24, 12))
            );
        }
        if (developerId == 2) {
            return List.of(
                    new Deployment(200, dt(2026, 4, 8, 16)),
                    new Deployment(201, dt(2026, 4, 15, 18)),
                    new Deployment(202, dt(2026, 4, 21, 16))
            );
        }
        return List.of(
                new Deployment(300, dt(2026, 4, 8, 12)),
                new Deployment(301, dt(2026, 4, 12, 12)),
                new Deployment(302, dt(2026, 4, 16, 13)),
                new Deployment(303, dt(2026, 4, 21, 16))
        );
    }

    private List<Bug> getBugs(Long developerId) {
        if (developerId == 1) {
            return List.of(
                    new Bug(1, dt(2026, 4, 12, 10))
            );
        }
        if (developerId == 2) {
            return List.of(
                    new Bug(2, dt(2026, 4, 14, 10)),
                    new Bug(3, dt(2026, 4, 26, 11))
            );
        }
        return List.of(
                new Bug(4, dt(2026, 4, 20, 11))
        );
    }

    private LocalDateTime dt(int year, int month, int day, int hour) {
        return LocalDateTime.of(year, month, day, hour, 0);
    }

    private record Issue(int id, LocalDateTime inProgressAt, LocalDateTime doneAt) {
    }

    private record PullRequest(int id, LocalDateTime createdAt, boolean merged) {
    }

    private record Deployment(int prId, LocalDateTime deployedAt) {
    }

    private record Bug(int id, LocalDateTime occurredAt) {
    }
}
