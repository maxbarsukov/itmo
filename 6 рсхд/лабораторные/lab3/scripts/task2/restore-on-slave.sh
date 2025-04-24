#!/usr/bin/env bash

export PGDATA=$HOME/ubi26
export PGWAL=$PGDATA/pg_wal
export PGLOCALE=ru_RU.UTF-8
export PGENCODE=UTF8
export PGUSERNAME=postgres2
export PGHOST=pg185
export LANG=ru_RU.UTF-8
export LC_ALL=ru_RU.UTF-8

initdb -D "$PGDATA" --encoding=$PGENCODE --locale=$PGLOCALE --lc-messages=$PGLOCALE --lc-monetary=$PGLOCALE --lc-numeric=$PGLOCALE --lc-time=$PGLOCALE --no-locale --username=$PGUSERNAME
pg_ctl -D $PGDATA -l $PGDATA/server.log start

echo "listen_addresses = '*'" >> $PGDATA/postgresql.conf
echo "port = 9114" >> $PGDATA/postgresql.conf

cat > $PGDATA/pg_hba.conf << EOF
# TYPE  DATABASE        USER            ADDRESS                 METHOD
host    all             all             127.0.0.1/32            trust
host    all             all             ::1/128                 trust
local   all             all                                     peer
EOF

pg_ctl -D $PGDATA restart

latest_backup=$(ls -t ~/backups/*.sql.gz | head -1)
unpacked_file="${latest_backup%.gz}"

gzip -d $latest_backup
psql -h localhost -p 9114 postgres -f $unpacked_file
