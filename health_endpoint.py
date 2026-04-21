# Mock health endpoint handler
def detailed_health():
    return {
        "status": "healthy",
        "services": {
            "database": "connected",
            "cache": "operational",
            "external_api": "accessible"
        }
    }
