# Лабораторная работа №4

## Вариант `368127`

<img alt="akira-kogami" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/akira-kogami.gif" height="280">

> [!TIP]
> Не утечка данных, а внеплановый децентрализованный бэкап!

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

## Задание

**Цель работы**: ознакомиться с методами и средствами построения отказоустойчивых решений на базе СУБД PostgreSQL; получить практические навыки восстановления работы системы после отказа.

Работа рассчитана на двух человек и выполняется в три этапа: настройка, симуляция и обработка сбоя, восстановление.

### Требования к выполнению работы

- В качестве хостов использовать одинаковые виртуальные машины.
- В первую очередь необходимо обеспечить сетевую связность между ВМ.
- Для подключения к СУБД (например, через `psql`), использовать отдельную виртуальную или физическую машину.
- Демонстрировать наполнение базы и доступ на запись на примере **не менее, чем двух** таблиц, столбцов, строк, транзакций и клиентских сессий.

### Этап 1. Конфигурация

Развернуть postgres на двух узлах в режиме трансляции логов. Не использовать дополнительные пакеты. Продемонстрировать доступ в режиме чтение/запись на основном сервере. Продемонстрировать, что новые данные синхронизируются на резервный сервер.

### Этап 2. Симуляция и обработка сбоя

#### 2.1 Подготовка

- Установить несколько клиентских подключений к СУБД.
- Продемонстрировать состояние данных и работу клиентов в режиме чтение/запись.

#### 2.2 Сбой

Симулировать ошибку диска на основном узле – **удалить** директорию `PGDATA` со всем содержимым.

#### 2.3 Обработка

- Найти и продемонстрировать в логах релевантные сообщения об ошибках.
- Выполнить переключение (`failover`) на резервный сервер.
- Продемонстрировать состояние данных и работу клиентов в режиме чтение/запись.

### Этап 3. Восстановление

- Восстановить работу основного узла – откатить действие, выполненное с виртуальной машиной на этапе **2.2**.
- Актуализировать состояние базы на основном узле – накатить все изменения данных, выполненные на этапе **2.3**.
- Восстановить исправную работу узлов в исходной конфигурации (в соответствии с **этапом 1**).
- Продемонстрировать состояние данных и работу клиентов в режиме чтение/запись.

---

### Требования к отчёту

Отчет должен быть **самостоятельным документом** (без ссылок на внешние ресурсы), содержать всю последовательность команд и исходный код скриптов по каждому пункту задания. Для демонстрации результатов приводить команду вместе с выводом (самой наглядной частью вывода, при необходимости).

### Вопросы для подготовки к защите

1. Синхронная и асинхронная репликация: отличия, ограничения и область применения.
2. Кластер в режиме Active-Active и Active-Standby: отличия, ограничения и область применения.
3. Балансировка нагрузки: описание и область применения.
4. От чего зависит время простоя системы в случае отказа?

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [postgrespro.ru/docs/postgresql/16/different-replication-solutions](https://postgrespro.ru/docs/postgresql/16/different-replication-solutions) | Отказоустойчивость, балансировка нагрузки и репликация. Сравнение различных решений |
| [postgrespro.ru/docs/postgresql/16/warm-standby](https://postgrespro.ru/docs/postgresql/16/warm-standby) | Трансляция журналов на резервные серверы |
| [postgrespro.ru/docs/postgresql/16/warm-standby-failover](https://postgrespro.ru/docs/postgresql/16/warm-standby-failover) | Отработка отказа (failover) |
| [postgrespro.ru/docs/postgresql/16/hot-standby](https://postgrespro.ru/docs/postgresql/16/hot-standby) | Горячий резерв (hot standby) |
| [severalnines.com/blog/what-look-if-your-postgresql-replication-lagging](https://severalnines.com/blog/what-look-if-your-postgresql-replication-lagging) | What to Look for if Your PostgreSQL Replication is Lagging |
| [cybertec-postgresql.com/en/services/postgresql-replication/synchronous-asynchronous-replication](https://www.cybertec-postgresql.com/en/services/postgresql-replication/synchronous-asynchronous-replication) | Synchronous and asynchronous replication |
| [cybertec-postgresql.com/en/monitoring-replication-pg_stat_replication](https://www.cybertec-postgresql.com/en/monitoring-replication-pg_stat_replication/) | Monitoring replication: `pg_stat_replication` |
| [medium.com/@gurungswastik1990/configuring-streaming-replication-in-postgresql-using-docker](https://medium.com/@gurungswastik1990/configuring-streaming-replication-in-postgresql-using-docker-46951f3d2fb7) | Configuring streaming replication in PostgreSQL, using Docker |
| [highgo.ca/2023/09/11/postgresql-load-balancing-made-easy-a-deep-dive-into-pgpool](https://www.highgo.ca/2023/09/11/postgresql-load-balancing-made-easy-a-deep-dive-into-pgpool/) | PostgreSQL Load Balancing Made Easy: A Deep Dive into `pgpool` |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
