#!/bin/sh

export PGDATA=$HOME/ubi26
export PGWAL=$PGDATA/pg_wal
export PGLOCALE=ru_RU.UTF-8
export PGENCODE=UTF8
export PGUSERNAME=postgres1
export PGHOST=pg186
export LANG=ru_RU.UTF-8
export LC_ALL=ru_RU.UTF-8

initdb -D "$PGDATA" --encoding=$PGENCODE --locale=$PGLOCALE --lc-messages=$PGLOCALE --lc-monetary=$PGLOCALE --lc-numeric=$PGLOCALE --lc-time=$PGLOCALE --no-locale --username=$PGUSERNAME

pg_ctl -D $PGDATA -l $PGDATA/server.log start
