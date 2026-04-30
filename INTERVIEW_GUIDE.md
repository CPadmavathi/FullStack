# Developer Productivity MVP - Interview Guide

## 2-3 Minute Walkthrough Script

"I built a Developer Productivity MVP focused on clarity and actionability.
The dashboard targets Individual Contributors and shows five core metrics:
Lead Time, Cycle Time, Bug Rate, Deployment Frequency, and PR Throughput.

The backend is Spring Boot with simple REST APIs.
For this assignment version, data is in-memory to keep setup fast and the logic interview-friendly.
Metrics are computed from timestamped records for issues, PRs, deployments, and bugs.

On top of metrics, I added a rule-based insight layer.
This translates numbers into plain-English interpretation and practical suggestions.
For example, high cycle time maps to potential blockers or oversized work items and suggests breaking tasks smaller.

I also added date-range filtering and a current-vs-previous trend section to support discussion of progress over time.
The design stays intentionally simple so every decision is easy to explain and defend."

## API Summary

- `GET /api/developer/{id}/profile`
- `GET /api/developer/{id}/metrics?from=YYYY-MM-DD&to=YYYY-MM-DD`
- `GET /api/developer/{id}/insights?from=YYYY-MM-DD&to=YYYY-MM-DD`

## Trade-Offs to Explain

- In-memory datasets instead of DB persistence
  - Pro: faster MVP delivery and demo reliability.
  - Con: not production-ready for real data scale.

- Rule-based insights instead of ML/LLM scoring
  - Pro: explainable and deterministic.
  - Con: less adaptive for complex behavior patterns.

- Single dashboard page
  - Pro: quick comprehension and lower implementation overhead.
  - Con: limited drill-down for deep diagnostics.

## 10 Follow-Up Questions (Practice)

1. Why did you prioritize these five metrics?
2. How do you avoid misinterpreting metrics without context?
3. Why choose rule-based insights over a machine learning model?
4. How would you tune thresholds for different teams?
5. What improvements would you ship in V2 first?
6. How would you persist and query this data at scale?
7. What if some timestamps are missing or inconsistent?
8. How would you prevent metric gaming behavior?
9. How would you add team-level and organization-level views?
10. What parts of this architecture would you split into services later?

## Crisp Answer Hints

- Keep saying: "This is MVP-first, explainability-first."
- Link each insight to a specific metric and threshold.
- Mention a clear V2 roadmap: DB, auth, team views, historical charts, alerts.
