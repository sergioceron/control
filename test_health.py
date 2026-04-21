import json
from health_endpoint import HealthEndpoint

def test_health_endpoint():
    endpoint = HealthEndpoint()
    
    detailed_health_info = endpoint.get_detailed_health()
    health_data = json.loads(detailed_health_info)
    
    assert "status" in health_data
    assert "uptime" in health_data
    assert "version" in health_data
    assert health_data["status"] == "healthy"
    assert float(health_data["uptime"]) >= 0.0
    assert health_data["version"] == "1.0.0"
    
    print("All tests passed!")

if __name__ == "__main__":
    test_health_endpoint()
