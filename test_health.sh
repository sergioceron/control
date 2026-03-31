#!/bin/sh

test_health_endpoint() {
    expected='{"status": "healthy", "service": "all systems operational"}'
    result=$(sh /tmp/health_endpoint.py)
    if [ "$result" != "$expected" ]; then
        echo "Test failed: expected $expected, got $result"
        return 1
    fi
    echo "Test passed"
    return 0
}

test_health_endpoint
