# Работа с терминалом

## 1. Конфигурация

```bash
$ mkdir ./build/
$ cd ./build/
$ cmake .. -DCMAKE_BUILD_TYPE=<config>
```

Вместо `<config>` можно использовать одну из существующих конфигураций:

- **`Debug`** быстро компилируется и подходит для разработки.
- **`ASan, LSan, MSan, UBSan`** подходят для отладки ошибок сегментации и других проблем с памятью. Рекомендуется 
  запустить ваш код с санитайзерами перед отправкой на проверку!
- **`Release`** нужен для сборки кода с оптимизациями и проверки скорости выполнения.

## 2. Сборка

```bash
$ cmake --build ./build/
```

Исполняемые файлы `./build/solution/image-transformer` и `./build/tester/image-matcher`
будут собраны, их можно использовать для ручного тестирования.

## 3. Тестирование

```bash
$ cmake --build ./build/ --target check
# ИЛИ
$ cd ./build/
$ ctest --output-on-failure
```

## Бонус: `ssh` + `git`

### Как настроить SSH ключи

```bash
$ ssh-keygen
$ cat ~/.ssh/id_rsa.pub
```

В настройках профиля GitLab нужно открыть категорию `SSH Keys`, добавить новый
ключ и скопировать содержимое `id_rsa.pub` туда.

### Как склонировать форк

```bash
$ git clone ssh://git@gitlab.se.ifmo.ru:4815/<my username>/assignment-image-rotation.git
$ cd ./assignment-image-rotation/
```

### Как отправить свои изменения обратно в форк

```bash
$ git add ./solution/
$ git status
$ git commit -m "Lab complete"
$ git push origin master
```

После того, как вы откроете merge request, каждое новое изменение, добавленное таким образом,
будет появляться там автомагически.

### Как обновить свою лабораторную, если преподаватель попросил "подтянуть к себе свежие изменения" из основного репозитория

```bash
$ git remote add upstream ssh://git@gitlab.se.ifmo.ru:4815/programming-languages/assignment-image-rotation.git
$ git fetch upstream
$ git checkout master
$ git merge upstream/master
$ git remote remove upstream
```
