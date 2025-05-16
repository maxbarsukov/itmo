# Лабораторная работа №4

## Вариант [`cv.maxbarsukov.ru`](https://cv.maxbarsukov.ru/); `4.1, 4.2, 4.3, 4.5 `

<img alt="lain-shizo" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/lain-shizo.gif" height="320">

> [!TIP]
> [Please don't pee in the pool.](https://web.archive.org/web/20160207203903/https://wiki.wireshark.org/#:~:text=Please%20don%27t%20pee%20in%20the%20pool.)

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

---

## Задание

**«Анализ трафика компьютерных сетей с помощью утилиты Wireshark»**

- Задание ЛР: [ЛР 4_Wireshark.pdf](./ЛР%204_Wireshark.pdf)

**Цель работы:** изучить структуру протокольных блоков данных, анализируя реальный трафик на компьютере студента с помощью бесплатно распространяемой утилиты Wireshark.

В процессе выполнения домашнего задания выполняются наблюдения за передаваемым трафиком с компьютера пользователя в Интернет и в обратном направлении. Применение специализированной утилиты Wireshark позволяет наблюдать структуру передаваемых кадров, пакетов и сегментов данных различных сетевых протоколов. При выполнении УИР рекомендуется выполнить анализ последовательности команд и определить назначение служебных данных, используемых для организации обмена данными в протоколах: ARP, DNS, FTP, HTTP, DHCP.

## Вариант

В качестве адреса сайта в заданиях следует использовать один из следующих URL (следует выбрать один из пунктов в порядке перечисления):

- Адрес сайта с домашней страницей студента. Автор страницы должен легко идентифицироваться с этой страницей по содержимому сайта.
- Адрес сайта, в название которого лексически входит фамилия студента (например: [www.sidorovivan.ru](www.sidorovivan.ru)).
- Адрес сайта, в котором по очереди встречаются инициалы (ФИО) студента в латинской транскрипции (например, для имени Иванов Фёдор Михайлович подойдёт адрес сайта [ifmo.ru](http://ifmo.ru)).

## Выполнение

Файлы-трассы (pcap) с сохраненным захваченным трафиком:

| Пункт | Файл |
| --- | --- |
| 4.1 | [pcap/ping.pcapng](./pcap/ping.pcapng) |
| 4.2 | [pcap/tracert.pcapng](./pcap/tracert.pcapng), [pcap/tracert-d.pcapng](./pcap/tracert-d.pcapng) |
| 4.3 | [pcap/http.pcapng](./pcap/http.pcapng) |
| 4.5 | [pcap/arp.pcapng](./pcap/arp.pcapng) |

## Защита

*TODO*

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [www.wireshark.org](https://www.wireshark.org/) <br> [www.wireshark.org/docs](https://www.wireshark.org/docs/) <br> [www.wireshark.org/download/docs](https://www.wireshark.org/download/docs/) | Официальный сайт Wireshark <br> Официальная документация Wireshark <br> Руководство пользователя Wireshark (PDF) |
| [apackets.com](https://apackets.com/) | Онлайн анализ PCAP-файлов |
| [wireshark.wiki](https://wireshark.wiki/) | Мануал Wireshark на русском |
| [habr.com/ru/articles/735866](https://habr.com/ru/articles/735866/) |Wireshark — подробное руководство по началу использования |
| [ru.wikipedia.org/wiki/Сетевая_модель_OSI](https://ru.wikipedia.org/wiki/%D0%A1%D0%B5%D1%82%D0%B5%D0%B2%D0%B0%D1%8F_%D0%BC%D0%BE%D0%B4%D0%B5%D0%BB%D1%8C_OSI) | Сетевая модель OSI |
| [datatracker.ietf.org/doc/html/rfc826](https://datatracker.ietf.org/doc/html/rfc826) <br> [datatracker.ietf.org/doc/html/rfc1035](https://datatracker.ietf.org/doc/html/rfc1035) <br> [datatracker.ietf.org/doc/html/rfc959](https://datatracker.ietf.org/doc/html/rfc959) | Спецификация ARP <br> Спецификация DNS <br> Спецификация FTP |
| [developer.mozilla.org/docs/Web/HTTP/Guides/Conditional_requests](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Conditional_requests) | HTTP conditional requests |
| [ru.wikipedia.org/wiki/Ping](https://ru.wikipedia.org/wiki/Ping) <br> [ru.wikipedia.org/wiki/ICMP](https://ru.wikipedia.org/wiki/ICMP) | Утилита `ping` <br> ICMP |
| [wiki.wireshark.org/practicaljokes](https://wiki.wireshark.org/practicaljokes) | *Приколы* |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
