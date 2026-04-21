#!/bin/sh

# Optimized Bubble Sort Function
bubble_sort() {
  array=("$@")
  n=${#array[@]}
  for ((i = 0; i < n - 1; i++)); do
    swapped=false
    for ((j = 0; j < n - i - 1; j++)); do
      if [ "${array[j]}" -gt "${array[j + 1]}" ]; then
        # Swap
        temp=${array[j]}
        array[j]=${array[j + 1]}
        array[j + 1]=$temp
        swapped=true
      fi
    done
    # If no two elements were swapped, break
    if ! $swapped; then
      break
    fi
  done
  echo "${array[@]}"
}

# Test Case
test_bubble_sort() {
  input=(64 34 25 12 22 11 90)
  expected_output="11 12 22 25 34 64 90"
  output=$(bubble_sort "${input[@]}")
  if [ "$output" = "$expected_output" ]; then
    echo "Test Passed"
  else
    echo "Test Failed: Expected $expected_output but got $output"
  fi
}

# Run Test Case
test_bubble_sort
