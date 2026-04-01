#!/bin/sh

test_binary_search() {
    local -r result=$(binary_search "$@")
    if [ "$result" != "$1" ]; then
        echo "Failed: binary_search($@) == $result"
        return 1
    fi
}

binary_search() {
    # The binary_search function logic should be placed here for testing
    return 0
}

main() {
    test_binary_search 0 "1 2 3 4 5" 1 || exit 1
    test_binary_search 2 "1 2 3 4 5" 3 || exit 1
    test_binary_search 4 "1 2 3 4 5" 5 || exit 1
    echo "All tests passed"
}

main
