#!/usr/bin/env bash

mkdir ~/restore

# Находим последний бэкап
latest_backup=$(ssh postgres2@pg185 "ls -t ~/backups/*.sql.gz | head -1")

# Копируем с резервного узла
scp postgres2@pg185:$latest_backup ~/restore/

# Распаковка и замена путей
gzip -d ~/restore/pgdump-*.sql.gz
sed -i '' 's|/var/db/postgres1/idd21|/var/db/postgres1/restore/idd21|g' ~/restore/pgdump-*.sql
sed -i '' 's|/var/db/postgres1/gzp28|/var/db/postgres1/restore/gzp28|g' ~/restore/pgdump-*.sql

# Создаем новые директории для табличных пространств
mkdir -p ~/restore/idd21 ~/restore/gzp28
