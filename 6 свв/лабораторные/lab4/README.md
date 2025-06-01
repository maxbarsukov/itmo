# Лабораторная работа №4

## Вариант `4`

<img alt="lain-intro" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/lain-intro.gif" height="320">

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

---

## Задание

**«Изучение работы контроллеров ввода/вывода»**

| Презентация | Шаблоны программы для Arduino | Пример настройки UART |
| --- | --- | --- |
| [task/lab4_slides.pdf](./task/lab4_slides.pdf) | [other/meteo_bmp280_i2c.ino](./other/meteo_bmp280_i2c.ino) <br> [other/meteo_bmp280_spi.ino](./other/meteo_bmp280_spi.ino) <br> [other/temp_dht11.ino](./other/temp_dht11.ino) | [other/uart_use_regs.ino](./other/uart_use_regs.ino) |

**Цель работы:** Познакомиться с принципами работы с контроллерами ввода
вывода на примере контроллера UART.

ПО для выполнения работы:

- [Arduino IDE](https://www.arduino.cc/en/software/#ide);
- [Logic 2](https://www.saleae.com/pages/downloads).

### Задание к лабораторной работе

1. Написать программу для микроконтроллера Atmega328, принимающую и отправляющую пакеты по интерфейсу UART в соответствии с обозначенным форматом пакета. Драйвер UART должен быть реализован с использованием операций ввода/вывода в регистры аппаратного контроллера UART.
2. Контроллер должен принимать данные с ПК, проверять их на корректность и отправлять обратно корректные пакеты. Если пакет пришел с ошибкой, то он отбрасывается.
3. Контроллер должен раз в секунду передавать данные с датчика, указанного в варианте задания.
4. Написать клиентскую программу на ПК для приема и отправки пакетов к микроконтроллеру по интерфейсу UART, моделирующей как корректную отправку пакетов, так и случаи с ошибками: неправильная длина, отсутствие синхробайта, недостаточное количество данных.
5. Подключить микроконтроллер к ПК и протестировать работоспособность написанных программ.
6. Снять осциллограмму передачи любого пакета по интерфейсу UART.
7. Оформить отчет по работе в электронном формате.

## Варианты

| № варианта | Датчик | Скорость UART | Четность | Кол-во стоповых бит |
| --- | --- | --- | --- | --- |
| 1 | BMP280, I2C: температура и давление | 19200 | even parity | 1 |
| 2 | BMP280, SPI: температура и давление | 38400 | odd parity | 2 |
| 3 | DHT11: температура и влажность | 57600 | even parity | 1 |
| 4 | DHT11: температура и влажность | 115200 | odd parity | 2 |

## Требования к отчету

1. На титульном листе должны быть приведены следующие данные:
    - Название дисциплины;
    - Номер и название лабораторной работы;
    - ФИО исполнителя и группа.
2. Во введении указываются цели и задачи работы.
3. В основной части приводится код микроконтроллера и клиентской программы для ПК. **Комментарии обязательны!**
4. Приводится временная диаграмма передачи одного пакета с проверкой контрольной суммы.

## Выполнение

- [`client.py`](./client.py) – Клиентская программа на ПК для приема и отправки пакетов к микроконтроллеру по интерфейсу UART;
- [`temp_dht11.ino`](./temp_dht11.ino) – Программа для микроконтроллера Atmega328, принимающую и отправляющую пакеты по интерфейсу UART для датчика DHT11;
- [`uart-packet.sal`](./uart-packet.sal) – Снятая в [Logic 2](https://www.saleae.com/pages/downloads) осциллограмма передачи пакета по интерфейсу UART.

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [ww1.microchip.com/downloads/en/DeviceDoc/Atmel-7810-...](https://ww1.microchip.com/downloads/en/DeviceDoc/Atmel-7810-Automotive-Microcontrollers-ATmega328P_Datasheet.pdf) | Atmega 328 Datasheet |
| [ru.wikipedia.org/wiki/UART](https://ru.wikipedia.org/wiki/%D0%A3%D0%BD%D0%B8%D0%B2%D0%B5%D1%80%D1%81%D0%B0%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%B0%D1%81%D0%B8%D0%BD%D1%85%D1%80%D0%BE%D0%BD%D0%BD%D1%8B%D0%B9_%D0%BF%D1%80%D0%B8%D1%91%D0%BC%D0%BE%D0%BF%D0%B5%D1%80%D0%B5%D0%B4%D0%B0%D1%82%D1%87%D0%B8%D0%BA) | UART |
| [habr.com/ru/articles/109395/](https://habr.com/ru/articles/109395/) | UART и с чем его едят |
| [www.arduino.cc/en/software/#ide](https://www.arduino.cc/en/software/#ide) | Arduino IDE |
| [www.saleae.com/pages/downloads](https://www.saleae.com/pages/downloads) | Logic 2 |
| [habr.com/ru/articles/758188/](https://habr.com/ru/articles/758188/) | Разбор I2S трафика в программе Logic 2 |
| [github.com/Imtjl/io-systems/arduino-atmega328-uart](https://github.com/Imtjl/io-systems/tree/main/arduino-atmega328-uart) | Выполнение 1-го варианта ЛР4 |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
