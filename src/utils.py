# Utility functions


def binary_search(arr, target):
    """
    Performs binary search on a sorted array.

    Parameters:
    arr (list): A list of sorted elements.
    target: The element to search for.

    Returns:
    int: The index of the target element if found, otherwise -1.
    """
    left, right = 0, len(arr) - 1
    while left <= right:
        mid = (left + right) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1
