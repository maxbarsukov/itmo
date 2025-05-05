#!/bin/bash
set -e

PRIMARY_HOST="primary"
PRIMARY_PORT=5432
PG_CONNECT_TIMEOUT=3
CHECK_INTERVAL=5

while true; do
    if ! pg_isready -h $PRIMARY_HOST -p $PRIMARY_PORT -t $PG_CONNECT_TIMEOUT -q; then
        echo "Primary is unavailable. Attempting promote..."

        if [ -f $PGDATA/standby.signal ]; then
            if pg_ctl promote -D $PGDATA; then
                echo "Promote successful. This node is now master."
                exit 0
            else
                echo "Promote failed!" >&2
                exit 1
            fi
        else
            echo "Not in standby mode. Cannot promote." >&2
            exit 1
        fi
    fi

    sleep $CHECK_INTERVAL
done
