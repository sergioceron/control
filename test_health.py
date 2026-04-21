import unittest
from health_endpoint import detailed_health_check

class TestHealthEndpoint(unittest.TestCase):
    def test_detailed_health_check(self):
        result = detailed_health_check()
        self.assertEqual(result['status'], "Service is running smoothly")
        self.assertEqual(result['uptime'], "72 hours, 34 minutes")
        self.assertEqual(result['version'], "v1.0.3")

if __name__ == '__main__':
    unittest.main()
