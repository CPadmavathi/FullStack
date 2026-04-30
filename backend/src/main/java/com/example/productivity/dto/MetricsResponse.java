package com.example.productivity.dto;

public record MetricsResponse(
        double leadTime,
        double cycleTime,
        double bugRate,
        int deploymentFrequency,
        int prThroughput
) {
}
