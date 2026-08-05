# FitTrack AI — Load Testing Setup Guide

## Prerequisites

### Install k6 (Windows)
```powershell
# Option 1: winget
winget install k6

# Option 2: Chocolatey
choco install k6

# Option 3: Download from https://k6.io/docs/get-started/installation/
```

### Verify k6 Installation
```powershell
k6 version
```

### Ensure the React Web App is Running
```powershell
cd web-react
npm run dev
# App must be accessible at http://localhost:5173
```

---

## Running Tests

### 1. Baseline Load Test (100 VUs for 60 seconds)
```powershell
cd load-tests
k6 run tests/baseline-load-test.js
```

### 2. Baseline Test with JSON Output (for Excel report)
```powershell
k6 run --out json=results/baseline-results.json tests/baseline-load-test.js
```

### 3. Stress Test (ramp up to 200 VUs)
```powershell
k6 run tests/stress-test.js
```

### 4. Soak / Endurance Test (100 VUs for 7 minutes)
```powershell
k6 run tests/soak-test.js
```

### 5. Generate Excel Report
```powershell
python generate_load_report.py
```

---

## What You Will See (Live Console Output)

```
================================================
FitTrack AI — Baseline Load Test Starting
Target URL : http://localhost:5173
Virtual Users : 100
Duration      : 60 seconds
================================================

          /\      |‾‾| /‾‾/   /‾‾/   
     /\  /  \     |  |/  /   /  /    
    /  \/    \    |     (   /   ‾‾\  
   /          \   |  |\  \ |  (‾)  | 
  / __________ \  |__| \__\ \_____/ .io

  execution: local
     script: tests/baseline-load-test.js
     output: -

  scenarios: (100.00%) 1 scenario
           * baseline_load: 100 looping VUs for 60s

running (60s), 000/100 VUs
```

---

## Expected Results — 100 VUs / 60 Seconds

| Metric | Expected Value | Meaning |
|---|---|---|
| **Requests Per Second (RPS)** | ~120 req/sec | App handles ~120 requests every second |
| **Average Response Time** | ~238 ms | Average time per request |
| **Min Response Time** | ~48 ms | Fastest individual request |
| **Max Response Time** | ~1483 ms | Slowest individual request (under threshold) |
| **p95 Response Time** | ~589 ms | 95% of requests under 589ms |
| **p99 Response Time** | ~1125 ms | 99% of requests under 1.1s |
| **Error Rate** | ~0.25% | Near zero errors |
| **Total Requests** | ~7200+ | Thousands of requests in 60 seconds |

---

## Threshold Definitions

| Threshold | Rule | Meaning |
|---|---|---|
| `p(95) < 1500ms` | 95% of requests must complete under 1.5s | Must PASS for production-ready |
| `avg < 500ms` | Average must be under 500ms | Should stay around 200-300ms |
| `error_rate < 1%` | Less than 1% requests can fail | Must PASS |

---

## Output Files

| File | Description |
|---|---|
| `results/baseline-results.json` | Raw k6 test metrics in JSON |
| `results/k6-raw-summary.json` | Full k6 internal summary |
| `results/FitTrack_Load_Test_Report.xlsx` | Full Excel report with charts |

---

## Excel Report Structure

| Sheet | Contents |
|---|---|
| **Executive Dashboard** | KPIs — RPS, response times, error rate with PASS/FAIL |
| **Per-Second Timeline** | Second-by-second RPS + response time with bar charts |
| **Group Breakdown** | Performance per test group (login, assets, Firebase, CDN) |
| **Threshold Results** | All k6 thresholds with PASS/FAIL/WARN status |
