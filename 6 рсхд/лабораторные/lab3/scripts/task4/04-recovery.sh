#!/usr/bin/env bash

# Остановка PostgreSQL для восстановления
pg_ctl -D $HOME/ubi26 stop

# Создаем recovery.conf
cat > $HOME/ubi26/recovery.conf <<EOF
restore_command = 'scp postgres2@pg185:~/wal_archive/%f %p'
recovery_target_time = '$SCHEMA_TIME'
recovery_target_action = 'promote'
EOF

# Запуск в режиме восстановления
pg_ctl -D $HOME/ubi26 -l $HOME/ubi26/recovery.log start

# Мониторинг процесса
tail -f $HOME/ubi26/recovery.log

# Ожидаем сообщение: "database system is ready to accept connections"
