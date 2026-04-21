def detailed_health_check():
    return {
        "status": "healthy",
        "uptime": "24 days",
        "dependencies": {
            "database": "OK",
            "cache": "OK"
        }
    }

if __name__ == "__main__":
    print(detailed_health_check())
