import unittest
from merge_sort import merge_sort

class TestMergeSort(unittest.TestCase):

    def test_empty_list(self):
        self.assertEqual(merge_sort([]), [])

    def test_single_element(self):
        self.assertEqual(merge_sort([1]), [1])

    def test_sorted_list(self):
        self.assertEqual(merge_sort([1, 2, 3]), [1, 2, 3])

    def test_unsorted_list(self):
        self.assertEqual(merge_sort([3, 1, 2]), [1, 2, 3])

    def test_duplicates(self):
        self.assertEqual(merge_sort([4, 5, 4, 1]), [1, 4, 4, 5])

    def test_negative_numbers(self):
        self.assertEqual(merge_sort([-2, -1, -3]), [-3, -2, -1])

if __name__ == '__main__':
    unittest.main()
