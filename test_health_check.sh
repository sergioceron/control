#!/bin/sh

# Test the health check endpoint

# URL for the local health check
URL="http://localhost:8080/health"

# Function to perform a health check request
check_health() {
  response=$(curl -s -w "%{http_code}" -o /dev/null "${URL}")
  if [ "$response" -ne 200 ]; then
    echo "Health check failed with status code: $response"
    exit 1
  else
    echo "Health check succeeded with status code: $response"
  fi
}

# Execute the health check
check_health

echo 'DONE'
