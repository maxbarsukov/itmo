# Как достать базу вопросов из ET1-файла?

> [!NOTE]
> Альтернативное решение по парсингу `.et1`/`.fdb` файлов: [github.com/Imtjl/net-parser](https://github.com/Imtjl/net-parser)

> [!TIP]
> **А где находятся эти тесты?** \
> Все тесты для eTester хранятся на Helios по пути `/export/public/ATI`, они выкладываются за 0-2 дня до тестирования.

1. Скачиваем с Helios все файлы теста (`.et1`, `.fdb` и `/pics`).
2. С помощью [`et2fdb.exe`](./et2fdb.exe) достаём базу вопросов из `et` в `fdb`.
3. Теперь запускаем скрипт [`to-upper-case.sh`](./to-upper-case.sh), чтобы все расширения картинок из `/pics` перевести в верхний регистр:

        bash to-upper-case.sh pics

> [!WARNING]
> **ОБРАТИТЕ ВНИМАНИЕ**, что [`decoder.py`](./decoder.py) не вытаскивает из теста **все** вопросы. Есть весь пул вопросов в целом, а скрипт достаёт именно те, которые будут встречаться на конкретном тесте. \
> Чтобы включить в файл **ВСЕ ВОПРОСЫ**, используйте [`decoder-updated.py`](./decoder-updated.py) (*создано [@HackMemory](https://github.com/HackMemory), отредактировано [@ColdDirol](https://github.com/ColdDirol)*), который извлекает из базы все вопросы, независимо от "группы".

4. Дальше в той же директории запускаем [`decoder.py`](./decoder.py) (*спасибо [@HackMemory](https://github.com/HackMemory/fdb-parser)!*), чтобы спарсить базу вопросов в HTML:

        python decoder <source>.fdb <target>.html

5. Полученный файлик неудобен для просмотра и поиска вопросов, поэтому можем использовать более подходящую альтернативу – [`viewer.html`](./viewer.html). Для этого берем из полученного в декодере HTML-файла JSON-объект `questionsData` и вставляем [сюда](https://github.com/maxbarsukov/itmo/blob/master/6%20%D1%81%D0%B5%D1%82%D0%B8/%D1%82%D0%B5%D1%81%D1%82%D1%8B/viewer.html#L22). Проверьте, что все картинки загружаются (`DevTools > Network`), при необходимости используйте [`to-upper-case.sh`](./to-upper-case.sh) / [`to-lower-case.sh`](./to-lower-case.sh) или измените расширение ссылок на картинки в HTML-файле.
6. Voi la! Смотрим `viewer.html`, читаем вопросы. По настроению [деплоим]((https://www.netlify.com/blog/2016/09/29/a-step-by-step-guide-deploying-on-netlify/)) итоговый HTML-файл и `/pics` на [netlify.com](https://www.netlify.com/).

---

*Спасибо [@ColdDirol](https://github.com/ColdDirol) за помощь с составлением гайда!*
