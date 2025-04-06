# Лабораторная работа 2

## Вариант `368114`

<img alt="digimon" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/digimon.gif" height="300">

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

## Задание

**Цель работы**: на выделенном узле создать и сконфигурировать новый кластер БД Postgres, саму БД, табличные пространства и новую роль, а также произвести наполнение базы в соответствии с заданием.

Отчёт по работе должен содержать все команды по настройке, скрипты, а также измененные строки конфигурационных файлов.

Способ подключения к узлу из сети Интернет через `helios`:

    ssh -J sXXXXXX@helios.cs.ifmo.ru:2222 postgresY@pgZZZ

Способ подключения к узлу из сети факультета:

    ssh postgresY@pgZZZ

Номер выделенного узла `pgZZZ`, а также логин и пароль для подключения Вам **выдаст преподаватель**.

### Этап 1. Инициализация кластера БД

<table>
<tr>
    <td>Директория кластера</td>
    <td><b><code>$HOME/ubi26</code></b></td>
</tr>
<tr>
    <td>Кодировка</td>
    <td><b>UTF8<b/></td>
</tr>
<tr>
    <td>Локаль</td>
    <td><b>русская</b></td>
</tr>
</table>

Параметры инициализации задать через **переменные окружения**.

### Этап 2. Конфигурация и запуск сервера БД

- Способы подключения:
    1. Unix-domain сокет в режиме `peer`;
    2. сокет TCP/IP, только `localhost`
- Номер порта: `9114`
- Способ аутентификации TCP/IP клиентов:
    - по паролю в открытом виде
- Остальные способы подключений запретить.
- Настроить следующие параметры сервера БД:
    - `max_connections`
    - `shared_buffers`
    - `temp_buffers`
    - `work_mem`
    - `checkpoint_timeout`
    - `effective_cache_size`
    - `fsync`
    - `commit_delay`
- Параметры должны быть подобраны в соответствии со сценарием OLTP:
    1. 1000 транзакций в секунду размером 32КБ;
    2. обеспечить высокую доступность (High Availability) данных.
- Директория WAL файлов: `$PGDATA/pg_wal`
- Формат лог-файлов: `.csv`
- Уровень сообщений лога: `NOTICE`
- Дополнительно логировать:
    1. завершение сессий;
    2. продолжительность выполнения команд.

### Этап 3. Дополнительные табличные пространства и наполнение базы

- Создать новые табличные пространства для партицированной таблицы:
    - `$HOME/idd21`;
    - `$HOME/gzp28`
- На основе `template0` создать новую базу: `fakebrownroad`.
- Создать новую роль, предоставить необходимые права, разрешить подключение к базе.
- От имени новой роли (не администратора) произвести наполнение **всех** созданных баз тестовыми наборами данных. **Все** табличные пространства должны использоваться по назначению.
- Вывести список **всех** табличных пространств кластера и содержащиеся в них объекты.

### Вопросы к защите лабораторной работы:

1. Способы запуска и остановки сервера PosgreSQL, их отличия.
2. Какие параметры локали сервера БД можно настроить? На что они влияют? Как и где их переопределить?
3. Конфигурационные файлы сервера. Способы изменения и применения конфигурации.
4. Что такое табличное пространство? Зачем нужны дополнительные табличные пространства?
5. Зачем нужны `template0` и `template1`?

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [pgtune.librasoft.by](https://pgtune.librasoft.by/) | Калькулятор параметров PostgresSQL (PGTune) |
| [youtube.com/watch?v=DVUP_xdEJNM](https://www.youtube.com/watch?v=DVUP_xdEJNM) | Разгоняем PostgreSQL. Личный опыт. |
| [postgresql.org/docs/current/runtime-config.html](https://www.postgresql.org/docs/current/runtime-config.html) <br> [postgrespro.ru/docs/postgresql/16/runtime-config](https://postgrespro.ru/docs/postgresql/16/runtime-config) | Конфигурация сервера |
| [postgresql.org/docs/current/runtime.html](https://www.postgresql.org/docs/current/runtime.html) <br> [postgrespro.ru/docs/postgresql/16/runtime](https://postgrespro.ru/docs/postgresql/16/runtime) | Подготовка к работе и сопровождение сервера |
| [postgresql.org/docs/current/creating-cluster.html](https://www.postgresql.org/docs/current/creating-cluster.html) <br> [postgrespro.ru/docs/postgresql/16/creating-cluster](https://postgrespro.ru/docs/postgresql/16/creating-cluster) | Создание кластера баз данных |
| [postgresql.org/docs/current/auth-pg-hba-conf.html](https://www.postgresql.org/docs/current/auth-pg-hba-conf.html) <br> [postgrespro.ru/docs/postgresql/16/auth-pg-hba-conf](https://postgrespro.ru/docs/postgresql/16/auth-pg-hba-conf) | Файл `pg_hba.conf` |
| [postgresql.org/docs/current/high-availability.html](https://www.postgresql.org/docs/current/high-availability.html) <br> [postgrespro.ru/docs/postgresql/16/high-availability](https://postgrespro.ru/docs/postgresql/16/high-availability) | Отказоустойчивость, балансировка нагрузки и репликация |
| [postgresql.org/docs/current/manage-ag-tablespaces.html](https://www.postgresql.org/docs/current/manage-ag-tablespaces.html) <br> [postgrespro.ru/docs/postgresql/16/manage-ag-tablespaces](https://postgrespro.ru/docs/postgresql/16/manage-ag-tablespaces) | Табличные пространства |
| [postgresql.org/docs/current/wal-intro.html](https://www.postgresql.org/docs/current/wal-intro.html) <br> [postgrespro.ru/docs/postgresql/16/wal-intro](https://postgrespro.ru/docs/postgresql/16/wal-intro) | Журнал предзаписи (WAL) |
| [sematext.com/blog/postgresql-performance-tuning/](https://sematext.com/blog/postgresql-performance-tuning/) | A Complete Guide to PostgreSQL Performance Tuning |
| [wiki.postgresql.org/wiki/Tuning_Your_PostgreSQL_Server](https://wiki.postgresql.org/wiki/Tuning_Your_PostgreSQL_Server) | Tuning Your PostgreSQL Server |
| [dragonflydb.io/faq/postgresql-cluster-locale](https://www.dragonflydb.io/faq/postgresql-cluster-locale) | How do you configure locale settings for a PostgreSQL cluster? |
| [aws.amazon.com/ru/compare/the-difference-between-olap-and-oltp/](https://aws.amazon.com/ru/compare/the-difference-between-olap-and-oltp/) | OLTP и OLAP – разница между системами обработки данных |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
