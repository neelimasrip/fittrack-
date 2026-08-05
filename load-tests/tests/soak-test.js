/**
 * FitTrack AI — Soak / Endurance Test (k6)
 * =========================================================
 * Tests system stability at baseline load over a longer period
 * to detect memory leaks or gradual degradation.
 *
 * Run:
 *   k6 run tests/soak-test.js
 * =========================================================
 */

import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:5173';
const errorRate = new Rate('error_rate');

export const options = {
  stages: [
    { duration: '1m',  target: 100 },  // Ramp up to baseline
    { duration: '5m',  target: 100 },  // Sustain baseline for 5 minutes
    { duration: '30s', target: 0   },  // Ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<1500', 'avg<500'],
    error_rate: ['rate<0.01'],
  },
};

export default function () {
  group('soak_page_load', () => {
    const res = http.get(BASE_URL);
    errorRate.add(res.status >= 400 || res.status === 0);
    check(res, {
      'App stable during soak': (r) => r.status === 200,
      'No degradation (under 1500ms)': (r) => r.timings.duration < 1500,
    });
  });

  sleep(0.5 + Math.random() * 0.5);
}

export function handleSummary(data) {
  const m = data.metrics;
  return {
    stdout: `
=========================================================
  FITTRACK AI — SOAK / ENDURANCE TEST SUMMARY
=========================================================
  VUs (Sustained)      : 100
  Duration             : ~7 minutes
  Requests/sec         : ${m.http_reqs?.values.rate?.toFixed(1) || 'N/A'}
  Avg Response Time    : ${m.http_req_duration?.values.avg?.toFixed(0) || 'N/A'} ms
  p95 Response Time    : ${m.http_req_duration?.values['p(95)']?.toFixed(0) || 'N/A'} ms
  Error Rate           : ${((m.error_rate?.values.rate || 0) * 100).toFixed(2)}%
  Total Requests       : ${m.http_reqs?.values.count || 'N/A'}
=========================================================
`,
    'results/soak-results.json': JSON.stringify(data, null, 2),
  };
}
