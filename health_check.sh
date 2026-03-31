#!/bin/sh

# Health Check Endpoint

echo 'Content-Type: application/json'
echo ''
echo '{"status": "OK", "timestamp": "'$(date '+%Y-%m-%dT%H:%M:%S%z')'"}'
