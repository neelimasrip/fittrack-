/**
 * FitTrack AI — Stress Test (k6)
 * =========================================================
 * Tests system behaviour under progressively increasing load
 * to find the breaking point beyond the 100-VU baseline.
 *
 * Run:
 *   k6 run tests/stress-test.js
 * =========================================================
 */

import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:5173';
const errorRate = new Rate('error_rate');

export const options = {
  stages: [
    { duration: '10s', target: 25  }, // Ramp up to 25 VUs
    { duration: '10s', target: 50  }, // Ramp up to 50 VUs
    { duration: '10s', target: 100 }, // Reach baseline of 100 VUs
    { duration: '15s', target: 150 }, // Exceed baseline — stress zone
    { duration: '10s', target: 200 }, // Peak stress
    { duration: '5s',  target: 0   }, // Ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<2000'],
    error_rate:        ['rate<0.05'],
  },
};

export default function () {
  group('app_load', () => {
    const res = http.get(BASE_URL);
    errorRate.add(res.status >= 400 || res.status === 0);
    check(res, {
      'App responding': (r) => r.status === 200,
      'Response under 2s': (r) => r.timings.duration < 2000,
    });
  });

  group('firebase_health', () => {
    const res = http.get(
      'https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=test'
    );
    errorRate.add(res.status === 0);
    check(res, {
      'Firebase reachable': (r) => r.status !== 0,
    });
  });

  sleep(Math.random() * 0.5 + 0.1);
}

export function handleSummary(data) {
  const m = data.metrics;
  return {
    stdout: `
=========================================================
  FITTRACK AI — STRESS TEST SUMMARY
=========================================================
  Peak VUs Reached     : 200
  Requests/sec         : ${m.http_reqs?.values.rate?.toFixed(1) || 'N/A'}
  Avg Response Time    : ${m.http_req_duration?.values.avg?.toFixed(0) || 'N/A'} ms
  p95 Response Time    : ${m.http_req_duration?.values['p(95)']?.toFixed(0) || 'N/A'} ms
  Error Rate           : ${((m.error_rate?.values.rate || 0) * 100).toFixed(2)}%
  Total Requests       : ${m.http_reqs?.values.count || 'N/A'}
=========================================================
`,
    'results/stress-results.json': JSON.stringify(data, null, 2),
  };
}
