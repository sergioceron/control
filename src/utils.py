# Quicksort algorithm implementation


def binary_search(arr, target):
    """
    Perform a binary search to find the index of target in sorted array arr.

    Returns the index of target if found, otherwise returns -1.
    """
    left, right = 0, len(arr) - 1
    while left <= right:
        mid = left + (right - left) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1
