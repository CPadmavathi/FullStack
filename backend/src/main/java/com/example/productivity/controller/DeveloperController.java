package com.example.productivity.controller;

import com.example.productivity.dto.DeveloperProfileResponse;
import com.example.productivity.dto.InsightsResponse;
import com.example.productivity.dto.MetricsResponse;
import com.example.productivity.service.InsightService;
import com.example.productivity.service.MetricsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/developer")
public class DeveloperController {

    private final MetricsService metricsService;
    private final InsightService insightService;

    public DeveloperController(MetricsService metricsService, InsightService insightService) {
        this.metricsService = metricsService;
        this.insightService = insightService;
    }

    @GetMapping("/{id}/profile")
    public DeveloperProfileResponse getProfile(@PathVariable("id") Long developerId) {
        return metricsService.getDeveloperProfile(developerId);
    }

    @GetMapping("/{id}/metrics")
    public MetricsResponse getMetrics(
            @PathVariable("id") Long developerId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return metricsService.getMetricsForDeveloper(developerId, from, to);
    }

    @GetMapping("/{id}/insights")
    public InsightsResponse getInsights(
            @PathVariable("id") Long developerId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        MetricsResponse metrics = metricsService.getMetricsForDeveloper(developerId, from, to);
        return insightService.generateInsights(metrics);
    }
}
