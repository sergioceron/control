# Testing script for health endpoint

def test_detailed_health_check():
    # Placeholder for testing logic
    expected = {
        'status': 'healthy',
        'components': {
            'database': 'operational',
            'cache': 'operational',
            'external_api': 'operational'
        }
    }
    response = detailed_health_check()
    assert response == expected, "Health check failed"

# Execute the test
test_detailed_health_check()
