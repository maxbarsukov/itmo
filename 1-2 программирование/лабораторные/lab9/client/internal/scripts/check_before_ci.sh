#!/bin/sh

RED=$(tput setaf 1)
GREEN=$(tput setaf 2)
RESET=$(tput sgr0)

abort()
{
    echo >&2 "$RED
************
**  FAIL  **
************
$RESET"
    echo "An error occurred. Exiting..." >&2
    exit 1
}

trap 'abort' 0
set -e

yarn_wrapper() {
  {
      echo "RUN: yarn $*"
      command yarn "$@" >&- 2>&-
  } || {
      printf "\tFailed, aborting;\n\tERROR CODE: %s\n" "$?"
      exit 1
  }
}

(
  export CI=true
  yarn_wrapper
  yarn_wrapper test -- --watchAll=false
  yarn_wrapper test:coverage -- --watchAll=false
  yarn_wrapper codecov

  yarn_wrapper lint
)

trap : 0

echo >&2 "$GREEN
************
**  DONE  **
************
$RESET"
