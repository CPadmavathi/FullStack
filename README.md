# Developer Productivity MVP

Minimal internship-ready MVP with:
- Spring Boot backend (metrics + insights APIs)
- React frontend (IC dashboard)
- In-memory sample datasets and computed metrics for easy demo

## Project Structure

- `backend` - Java Spring Boot API
- `frontend` - React app
- `ARCHITECTURE.md` - text architecture + folder structure
- `INTERVIEW_GUIDE.md` - 2-3 minute script + Q&A prep

## Run Backend

1. Open terminal in `backend`
2. Run: `mvn spring-boot:run`
3. Backend runs at `http://localhost:8080`

## Run Frontend

1. Open terminal in `frontend`
2. Run: `npm install`
3. Run: `npm run dev`
4. Frontend runs at `http://localhost:5173`

## APIs

- `GET /api/developer/{id}/metrics`
- `GET /api/developer/{id}/insights`
- `GET /api/developer/{id}/profile`
- Optional query params on both APIs: `from=YYYY-MM-DD&to=YYYY-MM-DD`

## Metric Logic (Simple MVP)

- Lead Time for Changes = average(`deployment_time - PR_created_time`)
- Cycle Time = average(`issue_done_time - issue_in_progress_time`)
- Bug Rate = `number_of_bugs / number_of_completed_issues`
- Deployment Frequency = `number_of_deployments`
- PR Throughput = `number_of_merged_PRs`

Example:
- `GET http://localhost:8080/api/developer/1/metrics`
- `GET http://localhost:8080/api/developer/1/insights`
- `GET http://localhost:8080/api/developer/1/profile`
- `GET http://localhost:8080/api/developer/1/metrics?from=2026-04-01&to=2026-04-30`
