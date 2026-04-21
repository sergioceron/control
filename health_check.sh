#!/bin/sh

timestamp=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
status="OK"

json_response=$(cat <<EOF
{
  "status": "$status",
  "timestamp": "$timestamp"
}
EOF
)

echo "$json_response"
