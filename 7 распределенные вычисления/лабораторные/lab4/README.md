### Сборка и запуск

#### Docker

```bash
./scripts/enter.sh

# внутри контейнера
./pa4 -p 3 --mutexl

# чтобы пересобрать
clang -std=c99 -Wall -pedantic *.c -o pa4 -L. -lruntime && chmod +x pa4
```

#### Локально

```bash
cd src
export LD_LIBRARY_PATH="../vendor:$LD_LIBRARY_PATH"
clang -std=c99 -Wall -pedantic *.c -L../vendor -lruntime -o pa4
./pa4 -p 3 --mutexl
```

### Архивация

Создаёт пригодный для отправки на проверку файл `pa4.tar.gz` со всеми `.c` и `.h` файлами:

```bash
./scripts/archive.sh
```
