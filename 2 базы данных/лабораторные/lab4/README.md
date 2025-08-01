# Лабораторная работа [№4](https://se.ifmo.ru/courses/db#lab4)

## Вариант `1501`

<img alt="bocchi-bocchi-the-rock" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/bocchi-bocchi-the-rock.gif" height="350">

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

### Задание

Составить запросы на языке SQL (пункты 1-2).

Для каждого запроса предложить индексы, добавление которых уменьшит время выполнения запроса (указать таблицы/атрибуты, для которых нужно добавить индексы, написать тип индекса; объяснить, почему добавление индекса будет полезным для данного запроса).

Для запросов 1-2 необходимо составить возможные планы выполнения запросов. Планы составляются на основании предположения, что в таблицах отсутствуют индексы. Из составленных планов необходимо выбрать оптимальный и объяснить свой выбор.
Изменятся ли планы при добавлении индекса и как?

Для запросов 1-2 необходимо добавить в отчет вывод команды `EXPLAIN ANALYZE [запрос]`

Подробные ответы на все вышеперечисленные вопросы должны присутствовать в отчете (планы выполнения запросов должны быть нарисованы, ответы на вопросы - представлены в текстовом виде).

## Запросы

1. **Запрос 1**

Сделать запрос для получения атрибутов из указанных таблиц, применив фильтры по указанным условиям:
`Н_ТИПЫ_ВЕДОМОСТЕЙ`, `Н_ВЕДОМОСТИ`.

Вывести атрибуты: `Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ`, `Н_ВЕДОМОСТИ.ДАТА`.

Фильтры (AND):
1. `Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ = Ведомость`.
2. `Н_ВЕДОМОСТИ.ЧЛВК_ИД > 163249`.

Вид соединения: `RIGHT JOIN`.

2. **Запрос 2**

Сделать запрос для получения атрибутов из указанных таблиц, применив фильтры по указанным условиям:

Таблицы: `Н_ЛЮДИ`, `Н_ОБУЧЕНИЯ`, `Н_УЧЕНИКИ`.

Вывести атрибуты: `Н_ЛЮДИ.ИМЯ`, `Н_ОБУЧЕНИЯ.ЧЛВК_ИД`, `Н_УЧЕНИКИ.ИД`.

Фильтры: (AND)
1. `Н_ЛЮДИ.ИМЯ < Александр`.
2. `Н_ОБУЧЕНИЯ.ЧЛВК_ИД > 163276`.
3. `Н_УЧЕНИКИ.ГРУППА = 3100`.

Вид соединения: `INNER JOIN`.

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [youtube.com/playlist?list=PLSE8ODhjZXjYCZfIbmEWH7f6MnYqyPwCE](https://www.youtube.com/playlist?list=PLSE8ODhjZXjYCZfIbmEWH7f6MnYqyPwCE) | CMU Special Topics in Database: Query Optimization / Не прям в тему лабы, но отличный материал по оптимизации SQL-запросов для любознательных |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
