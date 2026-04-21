def health_endpoint(request):
    status = request.get('status', '')
    if not isinstance(status, str):
        return {'error': 'Invalid status: must be a string'}, 400