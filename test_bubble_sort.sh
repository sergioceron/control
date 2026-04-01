#!/bin/sh

bubble_sort() {
  local array=("$@")
  local n=${#array[@]}
  local temp

  for ((i = 0; i < n; i++)); do
    for ((j = 0; j < n-i-1; j++)); do
      if [ "${array[j]}" -gt "${array[j+1]}" ]; then
        temp=${array[j]}
        array[j]=${array[j+1]}
        array[j+1]=$temp
      fi
    done
  done

  echo "${array[@]}"
}

test_bubble_sort() {
  local result

  result=$(bubble_sort 5 1 4 2 8)
  if [ "$result" != "1 2 4 5 8" ]; then
    echo "Test failed for input 5 1 4 2 8. Expected 1 2 4 5 8 but got $result"
    return 1
  fi

  result=$(bubble_sort 3 0 2 5 -1 4 1)
  if [ "$result" != "-1 0 1 2 3 4 5" ]; then
    echo "Test failed for input 3 0 2 5 -1 4 1. Expected -1 0 1 2 3 4 5 but got $result"
    return 1
  fi

  result=$(bubble_sort 10 9 8 7 6 5 4 3 2 1 0)
  if [ "$result" != "0 1 2 3 4 5 6 7 8 9 10" ]; then
    echo "Test failed for input 10 9 8 7 6 5 4 3 2 1 0. Expected 0 1 2 3 4 5 6 7 8 9 10 but got $result"
    return 1
  fi

  result=$(bubble_sort)
  if [ "$result" != "" ]; then
    echo "Test failed for empty input. Expected empty but got $result"
    return 1
  fi

  echo "All tests passed."
}

test_bubble_sort
