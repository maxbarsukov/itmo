#!/usr/bin/env bash

# Подтверждаем завершение восстановления
psql -h localhost -p 9114 postgres -c "SELECT pg_wal_replay_resume();"

# Удаляем сигнальный файл
rm $HOME/ubi26/recovery.signal

# Убираем параметры восстановления
sed -i '' '/^restore_command/d' $HOME/ubi26/postgresql.conf
sed -i '' '/^recovery_target_time/d' $HOME/ubi26/postgresql.conf

# Перезапускаем сервер
pg_ctl -D $HOME/ubi26 restart

# Проверить целостность данных
psql -h localhost -p 9114 -U data_user -d fakebrownroad -t -c "SELECT count(*) FROM table1;"
