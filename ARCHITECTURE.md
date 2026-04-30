# Developer Productivity MVP - Architecture

## Text-Based Diagram

```text
[React Dashboard (IC View)]
   |  GET /api/developer/{id}/profile
   |  GET /api/developer/{id}/metrics?from&to
   |  GET /api/developer/{id}/insights?from&to
   v
[Spring Boot REST Controller]
   v
[MetricsService]
   |- in-memory datasets (developers, issues, PRs, deployments, bugs)
   |- metric calculations
   v
[InsightService]
   |- simple rule engine (threshold-based insights + suggestions)
```

## Why This Is Good for MVP

- Small scope, fast to demo, easy to explain in interview.
- No heavy infra or model training required.
- Logic is transparent: every insight is traceable to a metric value.

## Backend Structure

```text
backend/src/main/java/com/example/productivity
  controller/
    DeveloperController.java
  service/
    MetricsService.java
    InsightService.java
  dto/
    DeveloperProfileResponse.java
    MetricsResponse.java
    InsightsResponse.java
  ProductivityMvpApplication.java
```

## Frontend Structure

```text
frontend/src
  App.jsx
  main.jsx
  styles.css
```
