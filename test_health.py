# Unit tests for the detailed health endpoint
def test_detailed_health():
    result = detailed_health()
    assert result["status"] == "healthy"
    assert result["services"]["database"] == "connected"
    assert result["services"]["cache"] == "operational"
    assert result["services"]["external_api"] == "accessible"
    print("All tests passed")

# Execute the test
test_detailed_health()
