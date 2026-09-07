# Базы данных

## 1. Как подключиться к Helios

Обычно в заданиях к лабораторным работам указано, что демонстрация их выполнения должна производиться на сервере se.ifmo.ru. На сервере установлена операционная система FreeBSD и программное обеспечение для работы с СУБД PostgreSQL.

Подключиться к серверу для работы в командной строке можно по протоколу SSH, а скопировать файлы со своего компьютера на сервер или с сервера к себе — по протоколу SCP или SFTP.

Параметры подключения:

<table>
  <tr>
    <td><strong>Host Name:</strong></td>
    <td>se.ifmo.ru (либо helios.cs.ifmo.ru)</td>
  </tr>
  <tr>
    <td><strong>Port:</strong></td>
    <td>2222</td>
  </tr>
  <tr>
    <td><strong>Login as (User name):</strong></td>
    <td rowspan="2">
      можно получить на странице
      <a href="https://se.ifmo.ru/passwd">https://se.ifmo.ru/passwd</a>
    </td>
  </tr>
  <tr>
    <td><strong>Password:</strong></td>
  </tr>
</table>

Это можно сделать как из командной строки со своего компьютера или с компьютера в учебной аудитории, так и с помощью одного из многочисленных графических клиентов, часть из которых приведена в таблице.

<table>
  <thead>
    <tr>
      <th>Название</th>
      <th>ОС</th>
      <th>Сайт</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>PuTTY</td>
      <td>Windows</td>
      <td><a href="https://www.chiark.greenend.org.uk/~sgtatham/putty/">https://www.chiark.greenend.org.uk/~sgtatham/putty/</a></td>
    </tr>
    <tr>
      <td>WinSCP</td>
      <td>Windows</td>
      <td><a href="https://winscp.net/">https://winscp.net/</a></td>
    </tr>
    <tr>
      <td>SmarTTY</td>
      <td>Windows</td>
      <td><a href="https://sysprogs.com/SmarTTY/">https://sysprogs.com/SmarTTY/</a></td>
    </tr>
    <tr>
      <td>Ásbrú CM</td>
      <td>Linux</td>
      <td><a href="https://www.asbru-cm.net/">https://www.asbru-cm.net/</a></td>
    </tr>
    <tr>
      <td>Muon</td>
      <td>Linux</td>
      <td><a href="https://github.com/subhra74/snowflake">https://github.com/subhra74/snowflake</a></td>
    </tr>
    <tr>
      <td>iTerm2</td>
      <td>MacOS</td>
      <td><a href="https://iterm2.com/">https://iterm2.com/</a></td>
    </tr>
    <tr>
      <td>Core Shell</td>
      <td>MacOS</td>
      <td><a href="https://codinn.com/shell/">https://codinn.com/shell/</a></td>
    </tr>
    <tr>
      <td>Hyper</td>
      <td>Win/Lin/Mac</td>
      <td><a href="https://hyper.is/">https://hyper.is/</a></td>
    </tr>
    <tr>
      <td>Tabby</td>
      <td>Win/Lin/Mac</td>
      <td><a href="https://tabby.sh/">https://tabby.sh/</a></td>
    </tr>
  </tbody>
</table>

Пример подключения из командной строки (Windows, Linux, MacOS) (вместо `s000000` нужно подставить свое имя пользователя — `s` + ваш номер ИСУ):

```bash
ssh -l s000000 -p 2222 se.ifmo.ru
# или
ssh -p 2222 s000000@se.ifmo.ru
```

При первом подключении обычно выдается сообщение о том, что невозможно проверить аутентичность сервера, так как его публичный ключ отсутствует в базе известных ключей. Нужно ответить `yes` на предложение добавить его в базу известных хостов.

```
The authenticity of host '[se.ifmo.ru]:2222 ([77.234.196.4]:2222)' can't be established.
ECDSA key fingerprint is SHA256:gwyf8uH0khUL3O4+0cHO2YPRQD0d2nonu/GlQjrkJoo.
Are you sure you want to continue connecting (yes/no/[fingerprint])? yes
Warning: Permanently added '[se.ifmo.ru]:2222' (ECDSA) to the list of known hosts.
```

Введите свой пароль на запрос `Password:`. Обратите внимание, что при вводе пароля ничего не отображается. Это нормально.

Для копирования файлов (например файла `schema.sql`) на
сервер Helios можно использовать команду `scp` (вместо `s000000` нужно подставить свое имя пользователя):

```bash
scp -P 2222 schema.sql s000000@se.ifmo.ru:~
# или в другую сторону
scp -P 2222 s000000@se.ifmo.ru:~/schema.sql .
```

У `scp` порт указывается заглавной `-P`, у ssh — строчной `-p`!

## 2. Как подключиться к PostgreSQL

Для начала создадим новый файл, например `test.sql`, и наполним его каким-нибудь содержимым:

```sql
DROP TABLE IF EXISTS test;
CREATE TABLE test (
    id BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL UNIQUE
);

INSERT INTO test (description) VALUES
    ('test1'),
    ('test2');

SELECT * FROM test;
```

Далее скопируем файл на Helios. Находясь в директории, где он лежит:

```bash
scp -P 2222 test.sql s000000@se.ifmo.ru:~/
```

И подключимся к Helios:

```bash
ssh -p 2222 s000000@se.ifmo.ru
```

Либо создадим файл прямо на Helios. Откроем редактор:

```bash
vi test.sql
```

И впишем сюда содержимое запроса, например, из начала раздела (чтобы начать писать в vi, нажмите клавишу `i`). Чтобы сохранить файл и выйти из редактора, нажмём `Esc` и наберём `:wq`, `Enter`. Если вы хотите выйти без сохранения наберите `:q!`.

Теперь зайдём в `psql`. В зависимости от лабораторной работы:

```bash
psql -h pg -d studs
# или
psql -h pg -d ucheb
```

Если по какой-либо причине имя пользователя нужно указать явно:

```bash
psql -h pg -U s000000 -d studs
```

Если вы видите приглашение вида `studs=>` — это успех!
Вы подключились к PostgreSQL.

Чтобы запустить скрипт без входа в `psql`:

```bash
psql -h pg -d studs -f test.sql
```

## 3. Основные команды psql

- `\i test.sql` — выполнить запросы из файла;
- `\d` — посмотреть структуры объектов базы данных;
- `\h` — посмотреть справку по SQL;
- `\?` — посмотреть справку по другим командам;
- `\q` — выйти из `psql`.

## 4. SSH-туннель

Хост `pg` доступен только **изнутри** Helios — напрямую с вашего компьютера он не виден. Чтобы работать с базой из локального `psql` или графического клиента, нужен SSH-туннель.

Проброс порта `ssh -L` работает с любым клиентом, в том числе с локальным `psql`. В отдельном окне терминала:

```bash
ssh -p 2222 -L 5433:pg:5432 s000000@se.ifmo.ru
```

В команде выше `5433` — это локальный порт на вашем компьютере, а `5432` — удаленный порт на сервере. Туннель перенаправляет трафик с локального порта на удаленный.

Пока это окно открыто, подключайтесь к `localhost:5433` как к обычной базе на своем компьютере:

```bash
psql -h localhost -p 5433 -d studs -U s000000
```

## 5. Подключение через графические клиенты

К PostgreSQL можно подключаться не только через `psql` в терминале, но и из графических клиентов.

В IntelliJ IDEA Ultimate / DataGrip, DBeaver и pgAdmin туннель настраивается прямо в свойствах подключения:

- **SSH**: хост `se.ifmo.ru`, порт `2222`, логин и пароль — те же, что для входа на Helios;
- **Data Source**: хост `pg`, порт `5432`, база `studs` или `ucheb`, пользователь — ваш `s000000`. Пароль можно найти в файле `.pgpass` на Helios. Посмотреть его содержимое можно командой `cat ~/.pgpass`. **Не публикуйте содержимое этого файла и не включайте его в отчёт** ~~или я снесу вашу лабу прямо перед парой~~.

В IDEA/DataGrip это опция *Use SSH tunnel* в настройках источника данных, в DBeaver — вкладка *SSH*, в pgAdmin — *SSH Tunnel* при регистрации сервера.

Учтите, что графический клиент удобен для просмотра данных, но **демонстрировать лабораторную всё равно нужно на сервере**, так что уметь работать в `psql` необходимо.
