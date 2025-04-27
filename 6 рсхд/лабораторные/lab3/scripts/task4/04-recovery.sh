#!/usr/bin/env bash

# Остановка PostgreSQL
pg_ctl -D $HOME/ubi26 stop

# Очистка каталога данных (ВАЖНО!)
rm -rf $HOME/ubi26/*
tar -xvf $HOME/base_backup/base.tar.gz -C $HOME/ubi26/

cat >> $HOME/ubi26/postgresql.conf <<EOF
restore_command = 'cp $HOME/wal_archive/%f %p'
recovery_target_time = '$SCHEMA_TIME'
EOF

# Создаем сигнальный файл для запуска в режиме восстановления
touch $HOME/ubi26/recovery.signal

# Запуск восстановления
pg_ctl -D $HOME/ubi26 -l $HOME/ubi26/recovery.log start

# Мониторинг процесса
tail -f $HOME/ubi26/recovery.log
