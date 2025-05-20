# Как достать базу вопросов из ET1-файла?

> [!NOTE]
> Альтернативное решение по парсингу `.et1`/`.fdb` файлов: [github.com/Imtjl/net-parser](https://github.com/Imtjl/net-parser)

> [!TIP]
> **А где находятся эти тесты?** \
> Все тесты для eTester хранятся на Helios по пути `/export/public/ATI`, они выкладываются за 0-2 дня до тестирования.

1. Скачиваем с Helios все файлы теста (`.et1`, `.fdb` и `/pics`).
2. С помощью [`et2fdb.exe`](./et2fdb.exe) достаём базу вопросов из `et` в `fdb`.
4. Теперь запускаем скрипт [`to-upper-case.sh`](./to-upper-case.sh), чтобы все расширения картинок из `/pics` перевести в верхний регистр:

        bash to-upper-case.sh pics

3. Дальше в той же директории запускаем [`decoder.py`](./decoder.py) (*спасибо [@HackMemory](https://github.com/HackMemory/fdb-parser)!*), чтобы спарсить базу вопросов в HTML:

        python decoder <source>.fdb <target>.html

5. Voi la! Смотрим HTML-файлик, читаем вопросы. По настроению деплоим всё это дело на [netlify.com](https://www.netlify.com/blog/2016/09/29/a-step-by-step-guide-deploying-on-netlify/).

---

*Спасибо [@ColdDirol](https://github.com/ColdDirol) за помощь с составлением гайда!*
