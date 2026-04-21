

def binary_search(arr, target):
    left, right = 0, len(arr) - 1
    while left <= right:
        mid = (left + right) // 2
        mid_val = arr[mid]
        if mid_val < target:
            left = mid + 1
        elif mid_val > target:
            right = mid - 1
        else:
            return mid
    return -1
