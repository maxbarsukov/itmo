# Лабораторная работа №4

## Вариант `привязан к ИСУ`

<img alt="GTO" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/gto.gif" height="300">

> [!TIP]
> Приложение для тестирования доступно только **во внутренней сети кафедры**.

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

---

## Задание

С помощью программного пакета [Apache JMeter](http://jmeter.apache.org/) провести нагрузочное и стресс-тестирование веб-приложения в соответствии с вариантом задания.

В ходе нагрузочного тестирования необходимо протестировать 3 конфигурации аппаратного обеспечения и выбрать среди них наиболее дешёвую, удовлетворяющую требованиям по максимальному времени отклика приложения при заданной нагрузке (в соответствии с вариантом).

В ходе стресс-тестирования необходимо определить, при какой нагрузке выбранная на предыдущем шаге конфигурация перестаёт удовлетворять требованиями по максимальному времени отклика. Для этого необходимо построить график зависимости времени отклика приложения от нагрузки.

### Параметры тестируемого веб-приложения

> [!NOTE]
> Если запрос содержит некорректные параметры, сервер возвращает **HTTP 403**. \
> Если приложение не справляется с нагрузкой, сервер возвращает **HTTP 503**.

- URL первой конфигурации ($ 3100) – [stload.se.ifmo.ru:8080?token=495351464&user=-2105800574&config=1](http://stload.se.ifmo.ru:8080?token=495351464&user=-2105800574&config=1);
- URL второй конфигурации ($ 4500) – [stload.se.ifmo.ru:8080?token=495351464&user=-2105800574&config=2](http://stload.se.ifmo.ru:8080?token=495351464&user=-2105800574&config=2);
- URL третьей конфигурации ($ 8300) – [stload.se.ifmo.ru:8080?token=495351464&user=-2105800574&config=3](http://stload.se.ifmo.ru:8080?token=495351464&user=-2105800574&config=3);
- Максимальное количество параллельных пользователей – 12;
- Средняя нагрузка, формируемая одним пользователем – 20 запр. в мин.;
- Максимально допустимое время обработки запроса – 520 мс.

### Отчёт по работе должен содержать

1. Текст задания.
2. Описание конфигурации JMeter для нагрузочного тестирования.
3. Графики пропускной способности приложения, полученные в ходе нагрузочного тестирования.
4. Выводы по выбранной конфигурации аппаратного обеспечения.
5. Описание конфигурации JMeter для стресс-тестирования.
6. График изменения времени отклика от нагрузки для выбранной конфигурации, полученный в ходе стресс-тестирования системы.
7. Выводы по работе.

### Вопросы к защите лабораторной работы

1. Тестирование системы целиком – **системное тестирование**.
2. Тестирование возможностей, стабильности, отказоустойчивости, совместимости.
3. Тестирование производительности – **CARAT**.
4. Альфа и Бета тестирование. Приемочное тестирование.
5. **Нагрузочное тестирование** – виды, цели и решаемые задачи.
6. Принципы реализации нагрузочного тестирования ПО.
7. Инструменты для реализации нагрузочного тестирования.
8. `Apache JMeter` – архитектура, поддерживаемые протоколы, особенности конфигурации.
9. **Стресс-тестирование** – основные понятия, виды стресс-сценариев.
10. Стресс-тестирование ПО. Виды стресс-тестов ПО. Тестирование ёмкости.

---

## Выполнение

Результаты тестирования:

| Нагрузочное тестирование | Стресс-тестирование |
|---|---|
| [load/result/results.csv](./load/result/results.csv) | [stress/result/results.csv](./stress/result/results.csv) |

### Как построить HTML-отчёт по результатам тестирования?

1. Сгенерировать `csv`-файл с результатами тестирования.
2. Создать файл конфигурации `user.properties` (может быть пустым).
3. Нажать `Tools -> Generate HTML Report`.
4. Указать путь для:
    - файла с таблицей тестирования;
    - файла настроек (`user.properties`);
    - директории, куда будет сохранён отчёт (должна будь пустой).

Также для генерации отчёта можно запустить такую команду:

    jmeter -n -t 'ваш-план-тестирования.jmx' -l 'результаты-тестирования.csv' -e -o 'директория-с-html-отчётом'

### Доступ к серверу `stload.se.ifmo.ru`

Пробрасываем порты из Helios через [эту команду](./bin/helios-port-forward). Порт может быть любым:

    ssh -f -N -L 8079:stload.se.ifmo.ru:8080 sXXXXXX@se.ifmo.ru -p 2222

После пробрасывания порта, в тестах всё равно нужно использовать `localhost` в качестве `url`.

#### Подключение через OpenVPN

Если по какой-то причине не получилось подключиться через SSH, можно подключиться к ресурсам факультета с помощью конфигурационного файла [`students.ovpn`](./other/students.ovpn).

Для авторизации используется имя пользователя (sXXXXXX) и пароль для Helios (тот, который на [se.ifmo.ru/passwd/](https://se.ifmo.ru/passwd/)).

Для студентов доступ ограничен только сервером helios и другими серверами с адресами в подсети `192.168.10.0/24`.
Поэтому чтобы послать запрос к `http://stload.se.ifmo.ru:8080`, используйте URL `http://192.168.10.83:8080`.

### Анализ стресс-тестирования

| Скрипт | Описание |
| --- | --- |
| [main_indicators.py](./stress/main_indicators.py) | Собирает основные показатели тестирования для более удобного ручного анализа |
| [autocorrelation.py](./stress/autocorrelation.py) | График автокорреляции для времени отклика |
| [concurrent_users.py](./stress/concurrent_users.py) | Анализ распределения параллельных пользователей |
| [error_clustering.py](./stress/error_clustering.py) | Кластеризация ошибок во временном пространстве |
| [error_domino_effect.py](./stress/error_domino_effect.py) | Анализ временных интервалов между ошибками |
| [error_type_distribution.py](./stress/error_type_distribution.py) | Динамика распределения ошибок по типам HTTP-статусов |
| [latency_analysis.py](./stress/latency_analysis.py) | Анализ времени отклика |
| [percentiles.py](./stress/percentiles.py) | Анализ перцентилей в динамике |
| [performance_anomalies.py](./stress/performance_anomalies.py) | Выявление аномалий в производительности |
| [performance_forecast.py](./stress/performance_forecast.py) | Прогнозирование деградации времени отклика |
| [thread_error_analysis.py](./stress/thread_error_analysis.py) | Распределение частоты ошибок между потоками |

---

## Полезные ссылки

| Ссылка | Описание |
|---|---|
| [jmeter.apache.org/](https://jmeter.apache.org/) | Сайт Apache JMeter |
| [jmeter-plugins.org/](https://jmeter-plugins.org/) | Плагины для JMeter |
| [jmeter.apache.org/usermanual/index.html](https://jmeter.apache.org/usermanual/index.html) | Официальная документация JMeter |
| [jmeter.apache.org/usermanual/generating-dashboard.html](https://jmeter.apache.org/usermanual/generating-dashboard.html) | Генерация дашбордов средствами JMeter |
| [habr.com/articles/514314/](https://habr.com/ru/companies/tbank/articles/514314/) | Анализ результатов нагрузочного тестирования |
| [www.blazemeter.com/blog/jmeter-tutorial](https://www.blazemeter.com/blog/jmeter-tutorial) | JMeter Tutorial: Getting Started With the Basics |
| [www.guru99.com/jmeter-performance-testing.html](https://www.guru99.com/jmeter-performance-testing.html) | How to Use JMeter for Performance & Load Testing |
| [apidog.com/blog/how-to-use-jmeter-for-stress-testing](https://apidog.com/blog/how-to-use-jmeter-for-stress-testing/) | Performance Stress Testing with JMeter: An Ultimate Guide |
| [www.softwaretestinghelp.com/types-of-software-testing/](https://www.softwaretestinghelp.com/types-of-software-testing/) | Разные типы тестирования ПО |
| [https://github.com/band-of-four/cheatsheets/testing/lab4.md](https://github.com/band-of-four/cheatsheets/blob/master/testing/lab4.md) | Ответы на вопросы с [se.ifmo.ru](https://se.ifmo.ru/courses/testing) |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии MIT](https://opensource.org/licenses/MIT). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
