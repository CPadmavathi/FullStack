package com.example.productivity.dto;

import java.util.List;

public record InsightsResponse(
        List<String> insights,
        List<String> suggestions
) {
}
