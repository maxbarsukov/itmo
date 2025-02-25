# Лабораторная работа 1

## Вариант `367081`

<img alt="Kita Ikuyo" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/kita-ikuyo.gif" height="220">


|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

## Задание

Используя сведения из системных каталогов, получить информацию о всех правилах (`CHECK` и `NOT NULL`) схемы:
1. Номер по порядку
2. Имя ограничения
3. Тип
4. Имя таблицы
5. Имя столбца
6. Текст ограничения.

```
        Имя ограничения           Тип Текст ограничения
-------------------------- -- -------------------------------------------------

1  AVCON_388143_СОСТО_000  C    Н_УЧЕНИКИ   СОСТОЯНИЕ  СОСТОЯНИЕ IN ('проект',
 'утвержден', 'отменен')

2  AVCON_388143_ПРИЗН_000  C    Н_УЧЕНИКИ   ПРИЗНАК    ПРИЗНАК IN ('обучен',
 'академ', 'повтор',
 'отчисл'

3  AVCON_378561_ПОЛ_000    C    Н_ЛЮДИ      ПОЛ        ПОЛ IN ('М', 'Ж')

4  AVCON_388176_ПОЛ_000    C    Н_ЛЮДИ      ПОЛ        ПОЛ IN ('М', 'Ж')

5  SYS_C0014067            C    Н_ЛЮДИ      ФАМИЛИЯ    "ФАМИЛИЯ" IS NOT NULL

6  AVCON_387864_ОЦЕНК_000  C    Н_ВЕДОМОСТИ ОЦЕНКА     ОЦЕНКА IN ('99', '5',
 '4', '3', '2', 'зачет',
 'незачет'

7  ВЕД_СОСТ_CHK            C    Н_ВЕДОМОСТИ СОСТОЯНИЕ  состояние IN
 ('актуальна',
 'неактуальна')
```

Программу оформить в виде **анонимного блока**.

### Вопросы к защите лабораторной работы:

1. Процедура, функция, анонимный блок: отличия, способы вызова, параметризация, вывод информации.
2. Системный каталог: описание, назначение, примеры использования.
3. Роли и права доступа.

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [www.postgresql.org/docs/current/overview.html](https://www.postgresql.org/docs/current/overview.html) | Overview of PostgreSQL Internals |
| [www.postgresql.org/docs/current/catalogs.html](https://www.postgresql.org/docs/current/catalogs.html) | System Catalogs |
| [www.postgresql.org/docs/current/information-schema.html](https://www.postgresql.org/docs/current/information-schema.html) | The Information Schema |
| [habr.com/ru/articles/813781/](https://habr.com/ru/articles/813781/) | PostgreSQL 16. Организация данных. Часть 1 |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/).

*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
