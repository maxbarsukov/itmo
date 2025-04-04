# Домашнее задание №1

## Вариант `БМА`

<img alt="Tomoko" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/tomoko-inspiration.gif" height="300">

> [!NOTE]
> Скремблирование – это как встретить динозавра. Ты либо встретишь, либо нет. Вот я один раз встретил. \
> — Тропченко Андрей Александрович

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

---

## Задание

| Описание | Конспект | Презентация |
| --- | --- | --- |
| [ДЗ_Описание.pdf](./ДЗ_Описание.pdf) | [ДЗ_Конспект_Кодирование.pdf](./ДЗ_Конспект_Кодирование.pdf) | [ДЗ_Кодирование.ppsx](./ДЗ_Кодирование.ppsx) |

**Цель работы:**

Изучение методов физического и логического кодирования, используемых в цифровых сетях передачи данных.

## Выполнение

- Программы для отрисовки диаграмм методов кодирования [AMI](./scripts/ami.py), [M2](./scripts/m2.py), [RZ](./scripts/rz.py) и [NRZ](./scripts/rz.py) лежат в папке [scripts/](./scripts/);
- Поиск оптимального полинома для скремблирвоания в [scripts/scramble.py](./scripts/scramble.py).

## Защита

Если ваш практик — [Тропченко А. А.](https://my.itmo.ru/persons/111848), то скорее всего на защите у вас спросят один из трёх вопросов:

1. **Можно ли к Манчестеру применять логическое/избыточное кодирование?** 

   Нет, не стоит так как у M2 нет постоянной составляющей и он самосинхронизирующийся.

2. **Что такое [PAM-5](https://ru.wikipedia.org/wiki/%D0%9F%D0%BE%D1%82%D0%B5%D0%BD%D1%86%D0%B8%D0%B0%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%BA%D0%BE%D0%B4_2B1Q)?**

   Кодируются не просто 0 и 1, а [-1, -2, 0, 1, 2]. Если попадаем на пятое состояние, то произошла какая-то помеха. Плюсы: скорость возрастает в 2 раза (так как в 2 раза больше значений).

3. **Минусы [скремблирования](https://ru.wikipedia.org/wiki/%D0%A1%D0%BA%D1%80%D0%B5%D0%BC%D0%B1%D0%BB%D0%B5%D1%80):**

   Необходимость выбрать правильный полином, так как ты не знаешь, подходящий он или нет. Может не полностью устранять некоторые паттерны, требует синхронизации скремблеров на обоих концах.

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [books.ifmo.ru/file/pdf/2275.pdf](https://books.ifmo.ru/file/pdf/2275.pdf) | Компьютерные сети и телекоммуникации: задания и тесты, Т.И. Алиев |
| [technologyuk.net/telecommunications/telecom-principles](https://www.technologyuk.net/telecommunications/telecom-principles/line-coding-techniques.shtml) | Теория по методам кодирования |
| [s265065.github.io/network-lab-solver](https://s265065.github.io/network-lab-solver/) | Решатор ДЗ (осторожно, бывают ошибки) |
| [github.com/RedGry/ITMO/Computer networks/LAB_1](https://github.com/RedGry/ITMO/tree/master/Computer%20networks/LAB_1) | Решение ДЗ от RedGry (задание старое и немного отличается) |
| [github.com/EgorMIt/ITMO/3 - Сети/СетиЛаб1.docx](https://github.com/EgorMIt/ITMO/blob/master/3%20-%20%D0%A1%D0%B5%D1%82%D0%B8/%D0%A1%D0%B5%D1%82%D0%B8%D0%9B%D0%B0%D0%B11.docx) | Решение ДЗ от EgorMIt (задание старое и немного отличается) |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
