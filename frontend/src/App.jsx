import { useEffect, useState } from "react";

const API_BASE = "http://localhost:8080/api/developer";

function MetricCard({ title, value }) {
  return (
    <div className="card">
      <div className="card-title">{title}</div>
      <div className="card-value">{value}</div>
    </div>
  );
}

export default function App() {
  const [developerId, setDeveloperId] = useState(1);
  const [profile, setProfile] = useState(null);
  const [fromDate, setFromDate] = useState("2026-04-01");
  const [toDate, setToDate] = useState("2026-04-30");
  const [metrics, setMetrics] = useState(null);
  const [previousMetrics, setPreviousMetrics] = useState(null);
  const [insights, setInsights] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      try {
        const currentParams = new URLSearchParams({ from: fromDate, to: toDate });
        const previousRange = getPreviousRange(fromDate, toDate);
        const previousParams = new URLSearchParams({
          from: previousRange.from,
          to: previousRange.to
        });

        const [metricsRes, insightsRes] = await Promise.all([
          fetch(`${API_BASE}/${developerId}/metrics?${currentParams.toString()}`),
          fetch(`${API_BASE}/${developerId}/insights?${currentParams.toString()}`)
        ]);
        const profileRes = await fetch(`${API_BASE}/${developerId}/profile`);
        const previousMetricsRes = await fetch(
          `${API_BASE}/${developerId}/metrics?${previousParams.toString()}`
        );
        const metricsData = await metricsRes.json();
        const insightsData = await insightsRes.json();
        const profileData = await profileRes.json();
        const previousMetricsData = await previousMetricsRes.json();

        setProfile(profileData);
        setMetrics(metricsData);
        setPreviousMetrics(previousMetricsData);
        setInsights(insightsData.insights || []);
        setSuggestions(insightsData.suggestions || []);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [developerId, fromDate, toDate]);

  function getPreviousRange(from, to) {
    const start = new Date(from);
    const end = new Date(to);
    const msInDay = 24 * 60 * 60 * 1000;
    const rangeDays = Math.max(1, Math.round((end - start) / msInDay) + 1);

    const previousTo = new Date(start.getTime() - msInDay);
    const previousFrom = new Date(previousTo.getTime() - (rangeDays - 1) * msInDay);

    return {
      from: previousFrom.toISOString().slice(0, 10),
      to: previousTo.toISOString().slice(0, 10)
    };
  }

  function trend(current, previous) {
    if (previous === null || previous === undefined) return "n/a";
    const diff = Number((current - previous).toFixed(2));
    if (diff > 0) return `+${diff}`;
    return `${diff}`;
  }

  return (
    <div className="container">
      <h1>Developer Productivity MVP</h1>
      <p className="subtitle">IC View - clear metrics, interpretation, actions</p>
      {profile && (
        <p className="subtitle">
          Developer: {profile.name} | Team: {profile.team}
        </p>
      )}

      <div className="toolbar">
        <label htmlFor="developerId">Developer ID</label>
        <select
          id="developerId"
          value={developerId}
          onChange={(e) => setDeveloperId(Number(e.target.value))}
        >
          <option value={1}>1 - Alex (Platform)</option>
          <option value={2}>2 - Priya (Payments)</option>
          <option value={3}>3 - Sam (Core)</option>
        </select>

        <label htmlFor="fromDate">From</label>
        <input
          id="fromDate"
          type="date"
          value={fromDate}
          onChange={(e) => setFromDate(e.target.value)}
        />

        <label htmlFor="toDate">To</label>
        <input
          id="toDate"
          type="date"
          value={toDate}
          onChange={(e) => setToDate(e.target.value)}
        />
      </div>

      {loading && <p>Loading...</p>}

      {!loading && metrics && (
        <>
          <section className="grid">
            <MetricCard title="Lead Time (days)" value={metrics.leadTime} />
            <MetricCard title="Cycle Time (days)" value={metrics.cycleTime} />
            <MetricCard title="Bug Rate" value={metrics.bugRate} />
            <MetricCard
              title="Deployment Frequency"
              value={metrics.deploymentFrequency}
            />
            <MetricCard title="PR Throughput" value={metrics.prThroughput} />
          </section>

          <section className="panel">
            <h2>Trend (Current vs Previous Range)</h2>
            <ul>
              <li>Lead Time: {trend(metrics.leadTime, previousMetrics?.leadTime)}</li>
              <li>Cycle Time: {trend(metrics.cycleTime, previousMetrics?.cycleTime)}</li>
              <li>Bug Rate: {trend(metrics.bugRate, previousMetrics?.bugRate)}</li>
              <li>
                Deployment Frequency:{" "}
                {trend(
                  metrics.deploymentFrequency,
                  previousMetrics?.deploymentFrequency
                )}
              </li>
              <li>
                PR Throughput: {trend(metrics.prThroughput, previousMetrics?.prThroughput)}
              </li>
            </ul>
          </section>

          <section className="panel">
            <h2>Insights</h2>
            <ul>
              {insights.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          </section>

          <section className="panel">
            <h2>Suggestions</h2>
            <ul>
              {suggestions.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          </section>
        </>
      )}
    </div>
  );
}
