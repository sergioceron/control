def test_health_check():
    assert health_check() == "Service is healthy", "Health check failed"

test_health_check()
