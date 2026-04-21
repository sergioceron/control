#!/bin/sh

# Function to generate health check JSON response
generate_health_check_response() {
  timestamp=$(date +"%Y-%m-%dT%H:%M:%S%z")
  echo "{\"status\": \"healthy\", \"timestamp\": \"$timestamp\"}"
}

# Endpoint to serve health check
serve_health_check() {
  while true; do
    # Listen on port 8080
    { echo -en "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n"; generate_health_check_response; } | nc -l -p 8080 -q 1;
  done
}

# Start serving health check
serve_health_check &
