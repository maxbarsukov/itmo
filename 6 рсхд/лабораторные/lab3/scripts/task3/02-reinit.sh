#!/usr/bin/env bash

# Создаем новый каталог для восстановления
NEW_DATA_DIR="$HOME/ubi26_recovery"
mkdir -p $NEW_DATA_DIR

# Инициализируем новую БД
initdb -D $NEW_DATA_DIR --encoding=UTF8 --locale=ru_RU.UTF-8 --lc-messages=ru_RU.UTF-8 --lc-monetary=ru_RU.UTF-8 --lc-numeric=ru_RU.UTF-8 --lc-time=ru_RU.UTF-8 --no-locale --username=postgres1
echo "listen_addresses = '*'" >> $NEW_DATA_DIR/postgresql.conf
echo "port = 9114" >> $NEW_DATA_DIR/postgresql.conf

# Запускаем PostgreSQL с новым каталогом
pg_ctl -D $NEW_DATA_DIR -l $NEW_DATA_DIR/server.log start

# Восстанавливаем данные (с учетом новых путей табличных пространств)
psql -h localhost -p 9114 postgres -f ~/restore/pgdump-*.sql
pg_ctl -D $NEW_DATA_DIR stop

# Перемещаем данные
rm -rf $HOME/ubi26
mv $NEW_DATA_DIR $HOME/ubi26

# Запускаем с оригинальным путем
pg_ctl -D $HOME/ubi26 start
