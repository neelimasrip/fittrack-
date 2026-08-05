import urllib.request, json
url = 'https://api.github.com/repos/neelimasrip/fittrack-/actions/runs'
req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
try:
    with urllib.request.urlopen(req, timeout=10) as response:
        data = json.loads(response.read().decode())
        runs = data.get('workflow_runs', [])
        if runs:
            latest_run = runs[0]
            print(f"Latest run ID: {latest_run['id']} - Status: {latest_run['status']} - Conclusion: {latest_run['conclusion']}")
            jobs_url = latest_run['jobs_url']
            with urllib.request.urlopen(urllib.request.Request(jobs_url, headers={'User-Agent': 'Mozilla/5.0'})) as jobs_resp:
                jobs_data = json.loads(jobs_resp.read().decode())
                for job in jobs_data.get('jobs', []):
                    if job['conclusion'] != 'success' and job['conclusion'] is not None:
                        print(f"Failed Job: {job['name']}")
                        for step in job['steps']:
                            if step['conclusion'] == 'failure':
                                print(f"  Failed Step: {step['name']}")
        else:
            print('No runs found')
except Exception as e:
    print(f'Error: {e}')
