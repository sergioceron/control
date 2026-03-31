def health_check():
    from datetime import datetime
    return {
        "status": "ok",
        "timestamp": datetime.now().isoformat()
    }
