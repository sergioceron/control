#!/bin/sh

# Test to verify health check endpoint

# Health check endpoint URL
HEALTH_CHECK_URL="http://localhost:8000/health"

# Perform the health check request
response=$(curl -sS -w "%{http_code}" -o /tmp/response.json "$HEALTH_CHECK_URL")
http_code=$(tail -n1 /tmp/response.json)
response_body=$(head -n -1 /tmp/response.json)

# Check HTTP status code
if [ "$http_code" -ne 200 ]; then
  echo "Health check endpoint returned HTTP status $http_code"
  exit 1
fi

# Check for expected JSON structure
if echo "$response_body" | grep -q '"status": "healthy"' &&
   echo "$response_body" | grep -q '"uptime":' &&
   echo "$response_body" | grep -q '"version":'; then
  echo "Health check endpoint returned expected JSON structure"
else
  echo "Unexpected JSON structure in health check response"
  exit 1
fi

echo "All health check tests passed"
exit 0
