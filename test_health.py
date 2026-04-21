def test_health_check():
    hc = health_check()
    assert hc['status'] == "ok"
    assert 'timestamp' in hc

def health_check():
    from datetime import datetime
    return {
        "status": "ok",
        "timestamp": datetime.now().isoformat()
    }

test_health_check()
