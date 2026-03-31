def get_service_status():
    # Placeholder for actual logic; assume always 'ok' for this example
    return "Service is running smoothly"

def get_uptime():
    # Placeholder for uptime logic; assume static duration
    return "72 hours, 34 minutes"

def get_version():
    # Placeholder for version logic; assume fixed version
    return "v1.0.3"

def detailed_health_check():
    status = get_service_status()
    uptime = get_uptime()
    version = get_version()
    return {
        "status": status,
        "uptime": uptime,
        "version": version
    }

if __name__ == "__main__":
    import json
    print(json.dumps(detailed_health_check()))
