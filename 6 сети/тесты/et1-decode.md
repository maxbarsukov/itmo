# Как достать базу вопросов из ET1-файла

> [!TIP]
> **А где находятся эти тесты?** \
> Все тесты для eTester хранятся на Helios по пути: `/export/public/ATI`, они выкладываются за 0-2 дня до тестирования.

1. Скачиваем с Helios файл с расширением `et1`.
2. С помощью [et2fdb.exe](./et2fdb.exe) достаём базу вопросов в `fdb`.
3. Дальше декодируем `fdb` файл через [decoder.py](./decoder.py) (*спасибо [@HackMemory](https://github.com/HackMemory/fdb-parser)!*) примерно так:

        python decoder.py base_250304.fdb index.html
4. Voi la! Смотрим `index.html`, читаем вопросы.
