def test_detailed_health_check():
    result = detailed_health_check()
    assert result["status"] == "healthy"
    assert "uptime" in result
    assert result["dependencies"]["database"] == "OK"
    assert result["dependencies"]["cache"] == "OK"

if __name__ == "__main__":
    test_detailed_health_check()
    print("All tests passed!")
