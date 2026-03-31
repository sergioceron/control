#!/bin/sh

# Health Check Endpoint
# Script to return JSON with system status and current timestamp

if [ "$1" = "/health" ]; then
    echo '{"status":"healthy","timestamp":'$(date +%s)'}'
    exit 0
fi

echo "Endpoint not found."
exit 1
