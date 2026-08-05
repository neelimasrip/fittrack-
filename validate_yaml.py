import yaml, sys
with open(".github/workflows/all-tests.yml", encoding="utf-8") as f:
    yaml.safe_load(f)
print("YAML syntax is valid.")
