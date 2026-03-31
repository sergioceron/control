import unittest
from health_endpoint import app

class HealthEndpointTest(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True

    def test_health_check(self):
        response = self.app.get('/health')
        self.assertEqual(response.status_code, 200)
        self.assertIn('healthy', response.data.decode())

if __name__ == '__main__':
    unittest.main()
