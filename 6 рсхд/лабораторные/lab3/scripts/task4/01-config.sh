#!/usr/bin/env bash

ssh postgres2@pg185 "mkdir ~/wal_archive"

psql -h localhost -p 9114 postgres <<EOF
ALTER SYSTEM SET wal_level = 'replica';
ALTER SYSTEM SET archive_mode = on;
ALTER SYSTEM SET archive_command = 'scp %p postgres2@pg185:~/wal_archive/%f';
EOF

pg_ctl -D $HOME/ubi26 restart
psql -h localhost -p 9114 postgres -c "SHOW archive_mode; SHOW archive_command;"
