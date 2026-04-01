def binary_search(arr, target):
    """
    Perform a binary search on a sorted array to find the index of the target value.
    
    :param arr: List of sorted elements.
    :param target: The element to search for in the list.
    :return: The index of the target element if found, otherwise -1.
    """
    left, right = 0, len(arr) - 1
    
    while left <= right:
        mid = (left + right) // 2
        mid_val = arr[mid]

        if mid_val == target:
            return mid
        elif mid_val < target:
            left = mid + 1
        else:
            right = mid - 1

    return -1
