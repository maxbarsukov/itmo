# Лабораторная работа 2

## Вариант `1`

<img alt="opening-pc" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/opening-pc.gif" height="350">

> [!NOTE]
> Бесклассовая междоменная маршрутизация — "сидр" буду называть, вино есть такое яблочное. Поэтому приятно так называть. \
> — Тауфик Измайлович Алиев

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

---

## Задание

«Моделирование компьютерных сетей в среде NetEmul. **Локальные сети**»

| Задание ЛР | Презентация |
| --- | --- |
| [ЛР 2_ЛВС.pdf](./ЛР%202_ЛВС.pdf) | [Презентация к ЛР2_ЛВС.pptx](./Презентация%20к%20ЛР2_ЛВС.pptx) |

**Цель работы:**

Изучение принципов настройки и функционирования локальных сетей, построенных с использованием концентраторов и коммутаторов, а также процессов передачи данных на основе стека протоколов TCP/IP, с использованием программы моделирования компьютерных сетей NetEmul.

## Выполнение

Разработанные модели локальных сетей:

| Этап | Модель |
| --- | --- |
| Этап 1. Построение сети с концентратором (hub) | [task-1.net](./networks/task-1.net)
| Этап 2. Построение локальной сети с коммутатором (switch) | [task-2.net](./networks/task-2.net)
| Этап 3. Многосегментная локальная сеть | [task-3.net](./networks/task-3.net)

## Защита

Если ваш практик — <a href="https://github.com/maxbarsukov/itmo/blob/master/.docs/tap-tap/README.md"><img alt="tropchenko" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/tap-tap/tropchenko.gif" height="20"></a> [Тропченко Андрей Александрович](https://my.itmo.ru/persons/111848), то скорее всего на защите у вас спросят один из этих вопросов:

1. **Можно ли использовать топологию «колько» в этапе 3?**

   Нет, так как это вызовет бесконечный цикл, в котором конценатратор и коммутатор бесконечно пересылают пакеты (см. [broadcast storms](https://ru.wikipedia.org/wiki/%D0%A8%D0%B8%D1%80%D0%BE%D0%BA%D0%BE%D0%B2%D0%B5%D1%89%D0%B0%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D1%88%D1%82%D0%BE%D1%80%D0%BC)).

2. **Что такое DHCP, DORA, что происходит после пересылки пакетов и т.д.**

   Читаем про [DHCP](https://ru.wikipedia.org/wiki/DHCP) и [DORA](https://en.wikipedia.org/wiki/Dynamic_Host_Configuration_Protocol#Operation).

---

## Полезные ссылки

> [!TIP]
> Для удаления элементов модели в NetEmul используйте `Ctrl+D`

| Ссылка | Описание |
| --- | --- |
| [netemul.sourceforge.net/help/ru/](https://netemul.sourceforge.net/help/ru/) | Руководство пользователя Netemul |
| [4_Рекомендации по работе с NetEmul-25.pdf](../../4_Рекомендации%20по%20работе%20с%20NetEmul-25.pdf) | Рекомендации по работе с программой NetEmul |
| [ru.wikipedia.org/wiki/Сетевой_концентратор](https://ru.wikipedia.org/wiki/%D0%A1%D0%B5%D1%82%D0%B5%D0%B2%D0%BE%D0%B9_%D0%BA%D0%BE%D0%BD%D1%86%D0%B5%D0%BD%D1%82%D1%80%D0%B0%D1%82%D0%BE%D1%80) | Сетевой концентратор |
| [ru.wikipedia.org/wiki/Сетевой_коммутатор](https://ru.wikipedia.org/wiki/%D0%A1%D0%B5%D1%82%D0%B5%D0%B2%D0%BE%D0%B9_%D0%BA%D0%BE%D0%BC%D0%BC%D1%83%D1%82%D0%B0%D1%82%D0%BE%D1%80) | Сетевой коммутатор |
| [geeksforgeeks.org/dhcp](https://www.geeksforgeeks.org/dynamic-host-configuration-protocol-dhcp/) | Dynamic Host Configuration Protocol (DHCP) |
| [geeksforgeeks.org/how-dora-works](https://www.geeksforgeeks.org/how-dora-works/) | How DORA Works? |
| [ru.wikipedia.org/wiki/TCP](https://ru.wikipedia.org/wiki/TCP) | TCP |
| [habr.com/ru/articles/253609](https://habr.com/ru/companies/rt-dc/articles/253609/) | «Идеальный шторм» и как это лечится (про broadcast storm)|

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
