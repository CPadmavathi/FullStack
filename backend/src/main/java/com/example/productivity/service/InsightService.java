package com.example.productivity.service;

import com.example.productivity.dto.InsightsResponse;
import com.example.productivity.dto.MetricsResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InsightService {

    public InsightsResponse generateInsights(MetricsResponse metrics) {
        List<String> insights = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        if (metrics.cycleTime() > 3.0) {
            insights.add("Cycle time is high -> tasks may be too large or blocked.");
            suggestions.add("Break stories into smaller tasks and track blockers daily.");
        }
        if (metrics.leadTime() > 4.0) {
            insights.add("Lead time is high -> review or release handoff may be slow.");
            suggestions.add("Set review SLA and automate post-merge deployment steps.");
        }
        if (metrics.bugRate() > 0.3) {
            insights.add("Bug rate is high -> quality checks may be insufficient.");
            suggestions.add("Add PR checklist and basic regression tests for critical flows.");
        }
        if (metrics.deploymentFrequency() < 3) {
            insights.add("Deployment frequency is low -> release process may be bottlenecked.");
            suggestions.add("Automate CI/CD pipeline and reduce manual release gates.");
        }
        if (metrics.prThroughput() < 8) {
            insights.add("PR throughput is low -> work may be blocked or PRs may be too large.");
            suggestions.add("Encourage smaller PRs and faster review turnaround.");
        }

        if (insights.isEmpty()) {
            insights.add("Metrics are in a healthy range for this period.");
            suggestions.add("Maintain current practices and monitor trends weekly.");
        }

        return new InsightsResponse(insights, suggestions);
    }
}
