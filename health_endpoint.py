# Health endpoint implementation

def detailed_health_check():
    # Mocked response for detailed health check
    return {
        'status': 'healthy',
        'components': {
            'database': 'operational',
            'cache': 'operational',
            'external_api': 'operational'
        }
    }
