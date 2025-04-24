#!/usr/bin/env bash

DATE=$(date +%Y-%m-%d-%H-%M-%S)

# Создание дампа
pg_dumpall -h localhost -p 9114 -U postgres1 | gzip -9 > ~/backups/pgdump-$DATE.sql.gz

# Перемещение на резервный хост
scp ~/backups/pgdump-$DATE.sql.gz postgres2@pg185:~/backups

# Удаление локальной копии
rm ~/backups/pgdump-$DATE.sql.gz

# Очистка старых архивов на резервном хосте
ssh postgres2@pg185 "find ~/backups -name '*.sql.gz' -mtime +28 -delete"
