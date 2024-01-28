#!/bin/bash --

usage="tester.sh --main-cmd '<cmd>' --tester-cmd '<cmd>' <test_name> [--log-dir '<dir>']"

POSITIONAL=()
while [ $# -gt 0 ]; do
    key="$1"; shift

    case $key in
        --main-cmd)
            main_cmd="$1"; shift
            ;;
        --tester-cmd)
            tester_cmd="$1"; shift
            ;;
        --log-dir)
            log_dir="$1"; shift
            ;;
        -h|--help)
            echo $usage
            exit
            ;;
        *)
            POSITIONAL+=("$key")
            ;;
    esac
done

set -- "${POSITIONAL[@]}"
test_name=$1
# Ignoring everything that is left

if [ -z "$main_cmd" ] || [ -z "$tester_cmd" ]; then
    echo "Error: --main-cmd and --tester-cmd are required." 1>&2
    echo $usage 1>&2
    exit 1
fi

if [ -z "$test_name" ]; then
    echo "Error: at least one positional argument is required." 1>&2
    echo $usage 1>&2
    exit 1
fi

if [ -z "$log_dir" ]; then
    log_out=/dev/stdout
    log_err=/dev/stderr
else
    mkdir -p "$log_dir"
    log_out="$log_dir/${test_name}_out.log"
    log_err="$log_dir/${test_name}_err.log"
fi


echo "********************************************************************************"
echo "$test_name: Started"

$main_cmd >"$log_out" 2>"$log_err"
rc=$?

if [ "$rc" -ne "0" ]; then
    echo
    echo "Failed at creating output. Command: $main_cmd"
    if [ ! -z "$log_dir" ]; then
	    [ -s "$log_out" ] && echo "*** stdout log: $log_out ***" && cat "$log_out"
	    [ -s "$log_err" ] && echo "*** stderr log: $log_err ***" && cat "$log_err"
    fi
    echo
    echo "$test_name: Failed with exit code $rc"
    echo "********************************************************************************"
    exit $rc
fi

$tester_cmd >$log_out 2>$log_err
rc=$?

if [ "$rc" -ne "0" ]; then
    echo
    echo "Actual and expected results differ. Command: $tester_cmd"
    if [ ! -z "$log_dir" ]; then
	    test -s "$log_out" && echo "*** stdout log: $log_out ***" && cat $log_out
	    test -s "$log_err" && echo "*** stderr log: $log_err ***" && cat $log_err
    fi
    echo
    echo "$test_name: Failed with exit code $rc"
    echo "********************************************************************************"
    exit $rc
fi

echo "$test_name: Finished successfully"
echo "********************************************************************************"
