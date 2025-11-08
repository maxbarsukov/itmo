# Лабораторная работа №2

## Вариант [`kudago.com`](https://kudago.com/), `kanban`

<img alt="angel-cop" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/angel-cop.gif" height="300">

> [!TIP]
> [xkcd.com/1658](https://xkcd.com/1658/)

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

---

## Задание

> [!NOTE]
> Лабораторную работу можно выполнять не только в [YouTrack](https://www.jetbrains.com/ru-ru/youtrack/download/get_youtrack.html#section=cloud), но и в других трекерах: [Jira](https://www.atlassian.com/software/jira), [Яндекс Трекере](https://tracker.yandex.ru/), [Redmine](https://www.redmine.org/) или любом другом. Главное — предварительно согласовать это с вашим практиком.

Зарегистрироваться для использования бесплатной облачной версии ПО [YouTrack](https://www.jetbrains.com/ru-ru/youtrack/download/get_youtrack.html#section=cloud) для управления своим программным проектом:

1. Создать учётные записи для всей своей проектной команды.
2. Интегрировать YouTrack с git репозиторием.
3. Настроить интеграцию с электронной почтой.
4. Создать проект с заданным в варианте профилем (Scrum / Kanban).
5. Настроить столбцы доски для своего проекта.
6. Создать план работ над проектом и зафиксировать его в YouTrack (создать спринты, релизы и задачи, а также необходимые для работы ветви в репозитории).
7. Симулировать процесс разработки проекта, постепенно закрывая "выполненные" задачи и открывая новые.
8. После завершения снять метрики проекта и предоставить отчет, содержащий описание процесса конфигурации и настройки, описание выбранного workflow, и сформированные с помощью YouTrack отчеты, отражающие статистику работы над проектом. Обязательно должны быть приведены: отчет по исполнителям, [burndown-диаграмма](https://en.wikipedia.org/wiki/Burndown_chart), отчет по времени, [диаграмма Гантта](https://en.wikipedia.org/wiki/Gantt_chart).

## Выполнение

> [!TIP]
> Заносить десятки, а то и сотни задач **вручную** будут только мазохисты, поэтому [«мы пойдём другим путём»](https://bigenc.ru/c/my-poidiom-drugim-putiom-91b4eb).

### Импорт задач в YouTrack из CSV-файла

Более подробно об импорте из CSV-файла можно прочитать в [**документации YouTrack**](https://www.jetbrains.com/help/youtrack/cloud/2025.3/import-from-csv.html).

- [tasks.csv](./tasks.csv) — выведенный путем проб и ошибок **пример успешно импортируемого CSV-файла с задачами**.

Вы можете импортировать данные из CSV-файла в YouTrack, используя в качестве посредника Google Spreadsheet. Сначала загрузите CSV-файл в электронную таблицу Google, а затем импортируйте данные из Google в YouTrack.

Учётная запись YouTrack, используемая для импорта, должна иметь достаточно прав для создания всех импортируемых сущностей. Для импорта рекомендуется использовать учётную запись с ролью `System Admin` или разрешением `Low-level Admin Write`.

Некоторые особенности:

- Значение в столбце `$type$` определяет тип импортируемой сущности.
- Задайте тип поля для столбца явно, добавив его к имени столбца в скобках. Это гарантирует, что YouTrack создаст настраиваемое поле нужного типа при импорте. Пример: `Stage (state)`.
- Если столбец содержит данные, которые можно преобразовать в даты, YouTrack импортирует их в значения `date and time`.
- Значения в столбцах с типом `user` обрабатываются как пользователи. YouTrack находит существующую учётную запись пользователя или создаёт новую для каждого значения пользователя.
- Значения типа `period` (для `Estimation` или `Spent time`) должны быть указаны в миллисекундах, YouTrack сам отобразит значение в неделях, днях, часах или минутах.
- Если YouTrack не связывает данные из столбца с каким-либо типом данных, он импортирует этот столбец как настраиваемое поле `string`.
- Если электронная таблица содержит ссылки на сущности, которых еще нет в YouTrack, они создаются.
- Парсер имен нечувствителен к регистру.

Перед настройкой импорта в YouTrack вам необходимо импортировать CSV-файл в электронную таблицу Google, настроить параметры общего доступа к электронной таблице и предварительно отформатировать данные.

**Чтобы импортировать CSV-файл в Google Таблицы** (более подробно в [документации](https://support.google.com/docs/answer/40608)):

1. В Google Таблицах откройте или создайте электронную таблицу.
2. В меню `Файл` выберите `Импортировать`.
3. Перейдите на вкладку `Добавить` и загрузите CSV-файл со своего компьютера.
4. Выберите место импорта и тип разделителя.
5. Нажмите Импорт данных. Ваш CSV-файл импортируется в электронную таблицу Google.

В настройках общего доступа к вашей таблице выберите параметр `Все, у кого есть ссылка`, и установите уровень доступа `Читатель` или выше.

Прежде чем добавлять новую конфигурацию импорта, необходимо создать API-ключ, связанный с проектом в Google. После этого вы сможете настроить импорт в YouTrack.

**Чтобы создать ключ API в Google** (более подробно в [документации](https://cloud.google.com/docs/authentication/api-keys)):

1. Откройте [Google Cloud Console](https://console.cloud.google.com/).
2. Войдите в систему, используя свою учетную запись Google.
3. Создайте новый проект или выберите существующий.
4. В меню `Navigation menu` слева выберите `APIs and services` → `Enabled APIs and services`.
5. Нажмите `Enable APIs and services`, найдите `Google Sheets API` и включите его для своего проекта.
6. Вернитесь на страницу `Enabled APIs and services`.
7. Нажмите `Enable APIs and services`, найдите `Google Drive API` и включите его для своего проекта.
8. В разделе `Credentials` нажмите `Create credentials` и выберите `API key`. Вы создали ключ API, связанный с вашей учетной записью Google и проектом в Google.
9. Скопируйте ключ API, чтобы ввести его в мастере настройки импорта.

**Чтобы настроить импорт из Google Таблиц:**

1. В главном навигационном меню выберите `Administration` → `Integrations` → `Imports`.
2. Нажмите `New import`, чтобы открыть диалоговое окно настройки.
3. Выберите `CSV`. Откроется диалоговое окно для нового импорта из CSV-файла. Далее вы будете перенаправлены на страницу настройки импорта из `Google Sheets`. Альтернативно сразу выберите опцию `Google Sheets`, не выбирая сначала `CSV`.
4. Нажмите кнопку `Next`. Откроется диалоговое окно импорта для нового импорта из Google Sheets .
5. Настройте параметры импорта: `Spreadsheet URL` — публичная ссылка на таблицу в Google Таблицах, `API key` — ключ API, который вы сгенерировали в своем аккаунте Google.
6. Выберите листы и целевые проекты YouTrack для импорта. Вы также можете выбрать, создать новый проект для импорта или импортировать данные в существующий проект YouTrack. YouTrack распознаёт, когда веб-адрес уже сопоставлен с исходным проектом при предыдущем импорте. Импортированные исходные проекты, которые уже сопоставлены в YouTrack, не могут быть выбраны. Вы можете создать несколько заданий импорта из одного и того же источника, не импортируя дублирующиеся данные.
7. После завершения нажмите кнопку `Start import`. YouTrack создаст проект с тем же именем, что и целевой лист в электронной таблице Google, и импортирует задачи, настраиваемые поля и пользователей (!). Если вы настроили сопоставление проектов, YouTrack создаст новые проекты и импортирует данные в существующие в соответствии с сопоставлением.

После настройки импорта он появится в списке `Imports`. Чтобы просмотреть конфигурацию и сведения об импорте, выберите нужную конфигурацию из списка.

На боковой панели сведений YouTrack отображает статус импорта и сведения об импортированных данных. Нажмите кнопку `Resume` на панели инструментов, чтобы начать опрос на наличие изменений и импорт обновлений из выбранных листов Google.

- Содержимое задач обновляется, когда поле `Updated` в импорте имеет более позднюю дату/время, чем `Updated` в соответствующих задачах YouTrack.
- Если поле `Updated` в импорте пустое, соответствующие задачи в YouTrack будут обновлены.
- Во время обновления добавляются только новые комментарии. Отредактированные комментарии не изменяются.

### Загрузка задач в YouTrack через REST API

Для автоматизированной загрузки задач можно использовать YouTrack REST API (описан в [документации](https://www.jetbrains.com/help/youtrack/devportal/api-howto-create-issue-with-fields.html), однако пример оттуда работать не будет, хихи).

Сначала нужно получить токен доступа: перейдите в `Profile` → `Account Security` → `Tokens` → `New token`, там введите имя токена, `Scope` можно оставить по умолчанию (`YouTrack` и `YouTrack Administration`). После получения токена скопируйте его, чтобы использовать в скрипте для загрузки задач.

TODO WIP

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [youtu.be/p4WU6cMMsls](https://youtu.be/p4WU6cMMsls) | Основы YouTrack |
| [jetbrains.com/.../mailbox-integration](https://www.jetbrains.com/help/youtrack/cloud/2025.3/mailbox-integration.html) | Интеграция YouTrack с электронной почтой (для **п.3**) |
| [wikipedia.org/wiki/Kanban](https://en.wikipedia.org/wiki/Kanban_(development)) | Kanban |
| [sberbusiness.live/publications/kanban](https://sberbusiness.live/publications/kanban) | Что такое Kanban и как использовать методологию в бизнесе |
| [wikipedia.org/wiki/Scrum](https://en.wikipedia.org/wiki/Scrum_(project_management)) | Scrum |
| [www.scrum.org](https://www.scrum.org/) | Ресурсы и руководства Scrum |
| [wikipedia.org/wiki/Burndown_chart](https://en.wikipedia.org/wiki/Burndown_chart) | Burndown-диаграмма |
| [habr.com/ru/articles/881006](https://habr.com/ru/articles/881006/) | Burndown chart: как он работает и зачем IT-команде участвовать в этом процессе |
| [wikipedia.org/wiki/Gantt_chart](https://en.wikipedia.org/wiki/Gantt_chart) | Диаграмма Ганта |
| [jetbrains.com/.../gantt-chart](https://www.jetbrains.com/help/youtrack/server/gantt-chart.html) | Диаграмма Ганта в YouTrack |
| [jetbrains.com/.../gantt-charts](https://www.jetbrains.com/help/youtrack/server/gantt-charts.html) | Интерактивные диаграммы Ганта в YouTrack |
| [jetbrains.com/.../reporting_analysis](https://www.jetbrains.com/ru-ru/youtrack/features/reporting_analysis.html) | Отчёты в YouTrack |
| [jetbrains.com/.../time-report](https://www.jetbrains.com/help/youtrack/server/time-report.html) | Отчёт по времени в YouTrack |
| [jetbrains.com/.../time-management](https://www.jetbrains.com/help/youtrack/server/time-management-tutorial.html) | Time Tracking в YouTrack |
| [jetbrains.com/.../api-use-cases](https://www.jetbrains.com/help/youtrack/devportal/api-use-cases.html) | YouTrack REST API |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии MIT](https://opensource.org/licenses/MIT). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
