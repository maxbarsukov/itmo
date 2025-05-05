# Как запустить?

> При повторном запуске стоит начать с:
>
> ```bash
> docker-compose down -v
> sudo rm -rf ./primary/data/ ./standby/data/
> ```

## Запуск

Запуск `primary`:

```bash
docker-compose up -d --build primary
```

Ожидаем лога `database system is ready to accept connections`.

Запуск [`init-primary.sh`](./primary/init/init-primary.sh):

```bash
docker exec -it primary bash -c "/home/init/init-primary.sh"
docker restart primary
```

Запуск `standby`:

```bash
docker-compose up -d --build standby
```

При работающем `standby` запустим скрипт автоматического promote:

```bash
docker exec -d standby bash -c "/home/scripts/auto_promote.sh"
```

## Проверка

Проверим данные на `primary`:

```bash
docker exec -it primary psql -U postgres -d test -c "SELECT * FROM users;"
docker exec -it primary psql -U postgres -d test -c "INSERT INTO users (name) VALUES ('Bradley');"
```

Проверим режим чтения на `standby`:
```bash
docker exec -it standby psql -U postgres -d test -c "SELECT * FROM users;"
```

Попробуем записать данные на `standby` (должна быть ошибка):

```bash
docker exec -it standby psql -U postgres -d test -c "INSERT INTO users (name) VALUES ('Bradley');"
```

## Симуляция работы

Запуск:

```bash
docker exec -it primary bash /home/scripts/read_client.sh
docker exec -it primary bash /home/scripts/write_client.sh
docker exec -it standby bash /home/scripts/read_client.sh
```

Данные в обоих клиентах будут одинаковыми.

## Сбой мастера

Симулируем ошибку диска на основном узле - удалим директорию `PGDATA` со всем содержимым.

```bash
docker exec -it primary bash -c "rm -rf /var/lib/postgresql/data"
```

При этом чтение в `primary` будет невозможно:

```bash
docker exec -it primary bash /home/scripts/read_client.sh
```

А чтение в `standby` продолжится:

```bash
docker exec -it standby bash /home/scripts/read_client.sh
```

## Обработка

Проверим чтение и запись на `standby` после `promote` (автоматический, запустить вручную: `docker exec -it standby psql -U postgres -d test -c "SELECT pg_promote();"`):

```bash
docker exec -it standby psql -U postgres -d test -c "INSERT INTO users (name) VALUES ('Charlie');"
docker exec -it standby psql -U postgres -d test -c "SELECT * FROM users;"
```

## Восстановление

Восстанавливаем работу `primary`:

```bash
docker exec -it primary bash
```

В консоли `primary`:

```bash
su
su postrges
```
```bash
pg_basebackup -P -X stream -c fast -h standby -U replicator -D ~/backup
# password: replicator_password
rm -rf /var/lib/postgresql/data/
mv ~/backup/* /var/lib/postgresql/data/
```
Выходим из контейнера `primary`.

В консоли `standby`:

```bash
docker exec -it standby bash
```

```bash
touch /var/lib/postgresql/data/standby.signal
```

Выходим из контейнера `standby`.

Останавливаем `standby`:

```bash
docker-compose stop standby
sudo rm -rf ./standby/data/
```

Заново запускаем `primary`:

```bash
docker-compose up -d primary
```

Пересоздаем `standby`:

```bash
docker-compose up -d --build standby
```

Проверяем:

```bash
docker exec -it primary bash /home/scripts/read_client.sh
docker exec -it primary bash /home/scripts/write_client.sh
docker exec -it standby bash /home/scripts/read_client.sh
```
