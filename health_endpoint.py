import json
import time

class HealthEndpoint:
    def __init__(self):
        self.start_time = time.time()
        self.version = "1.0.0"
    
    def get_service_status(self):
        # Dummy service status, ideally this would check real service health
        return "healthy"
    
    def get_uptime(self):
        uptime_seconds = time.time() - self.start_time
        return str(uptime_seconds)
    
    def get_version(self):
        return self.version
    
    def get_detailed_health(self):
        health_info = {
            "status": self.get_service_status(),
            "uptime": self.get_uptime(),
            "version": self.get_version()
        }
        return json.dumps(health_info)

if __name__ == "__main__":
    health_endpoint = HealthEndpoint()
    print(health_endpoint.get_detailed_health())

