# Задания

### Вопрос

Посмотрите, какие у вас запущены процессы. Выберите один (например, оболочку, которой вы пользуетесь) и посмотрите на содержимое директории `/proc/PID/`, где `PID` — его идентификатор.

    ls /proc/$$

### Вопрос

Что внутри файла `/proc/PID/environ`?

    cat /proc/$$/environ

Переменные окружения, доступные для процесса.

### Вопрос

Прочитайте про запуск программ в фоне. Что делают команды `bg`, `fg`, `jobs`?

- `jobs` выводит список фоновых процессов;
- `fg номер` переводит процесс на передний план;
- `bg номер` переводит процесс на задний план;

Напишем [простую программу](./questions/q1.asm), которая входит в бесконечный цикл:

```nasm
section .data
correct: dq -1
section .text
global _start
_start:
jmp _start
```

Что она делает? Скомпилируйте и запустите её в фоне.

    nasm -g questions/q1.asm -felf64 -o hello.o
    ld -o hello hello.o
    ./hello &


### Вопрос

Выведите с помощью `cat` содержимое файла `/proc/PID/maps`, где `PID` — идентификатор процесса, который вы запустили в фоне.

```
00400000-00401000 r--p 00000000 103:07 6837558              /home/max/prog/itmo/itmo/3 япы/hello
00401000-00402000 r-xp 00001000 103:07 6837558              /home/max/prog/itmo/itmo/3 япы/hello
00402000-00403000 rw-p 00002000 103:07 6837558              /home/max/prog/itmo/itmo/3 япы/hello
7ffd35ac5000-7ffd35ae7000 rw-p 00000000 00:00 0             [stack]
7ffd35b90000-7ffd35b94000 r--p 00000000 00:00 0             [vvar]
7ffd35b94000-7ffd35b96000 r-xp 00000000 00:00 0             [vdso]
ffffffffff600000-ffffffffff601000 --xp 00000000 00:00 0     [vsyscall]
```

### Вопрос

Почему в регионах начальный и конечный адрес в 16-ричном формате заканчиваются всегда на три нуля?

Все разбивается на виртуальную память по 4КБ каждая, и адреса должны быть кратны 4КБ. Если посчитать это в hex, то выйдет, что xxxx000 это 4096 адресов, а значит это просто гарант делимости адреса на 4КБ, чтобы страницу можно было размещать.

### Вопрос

Определите, по каким адресам загружаются секции `.text` и `.data` из примера. Вам может помочь `readelf` и таблица символов.

```
Таблица символов «.symtab» содержит 7 элементов:
   Чис:    Знач           Разм Тип     Связ   Vis      Индекс имени
     0: 0000000000000000     0 NOTYPE  LOCAL  DEFAULT  UND 
     1: 0000000000000000     0 FILE    LOCAL  DEFAULT  ABS q1.asm
     2: 0000000000402000     8 OBJECT  LOCAL  DEFAULT    2 correct
     3: 0000000000401000     0 NOTYPE  GLOBAL DEFAULT    1 _start
     4: 0000000000402008     0 NOTYPE  GLOBAL DEFAULT    2 __bss_start
     5: 0000000000402008     0 NOTYPE  GLOBAL DEFAULT    2 _edata
     6: 0000000000402008     0 NOTYPE  GLOBAL DEFAULT    2 _end
```

Помня, что `Value` хранит информацию о 2 значениях, обратим внимание лишь на вторую половину(она отвечает за адрес). Видно, что секции могут начинаться с `401`, а могут с `402`. Видимо это и есть то, что от нас просят.

Можно еще написать `readelf -l`, тогда выводятся секции

```
 Соответствие раздел-сегмент:
  Сегмент Разделы...
   00     
   01     .text 
   02     .data 
```

### Вопрос

Определите хотя бы один запрещённый диапазон адресов.

Список всех зарезервированных областей памяти в системе можно просмотреть командой `lspci -m`:

```
00:00.0 "Host bridge" "Intel Corporation" "11th Gen Core Processor Host Bridge/DRAM Registers" -r01 -p00 "ASUSTeK Computer Inc." "11th Gen Core Processor Host Bridge/DRAM Registers"
00:02.0 "VGA compatible controller" "Intel Corporation" "TigerLake-LP GT2 [Iris Xe Graphics]" -r01 -p00 "ASUSTeK Computer Inc." "TigerLake-LP GT2 [Iris Xe Graphics]"
00:04.0 "Signal processing controller" "Intel Corporation" "TigerLake-LP Dynamic Tuning Processor Participant" -r01 -p00 "ASUSTeK Computer Inc." "TigerLake-LP Dynamic Tuning Processor Participant"
00:07.0 "PCI bridge" "Intel Corporation" "Tiger Lake-LP Thunderbolt 4 PCI Express Root Port #0" -r01 -p00 "ASUSTeK Computer Inc." "Tiger Lake-LP Thunderbolt 4 PCI Express Root Port"
00:07.2 "PCI bridge" "Intel Corporation" "Tiger Lake-LP Thunderbolt 4 PCI Express Root Port #2" -r01 -p00 "" ""
00:08.0 "System peripheral" "Intel Corporation" "GNA Scoring Accelerator module" -r01 -p00 "ASUSTeK Computer Inc." "GNA Scoring Accelerator module"
00:0d.0 "USB controller" "Intel Corporation" "Tiger Lake-LP Thunderbolt 4 USB Controller" -r01 -p30 "" ""
00:0d.2 "USB controller" "Intel Corporation" "Tiger Lake-LP Thunderbolt 4 NHI #0" -r01 -p40 "Unknown vendor 2222" "Tiger Lake-LP Thunderbolt 4 NHI"
...
```

### Вопрос

Что такое inode в файловой системе?

**Inode** или **I-node** или **индексный дескриптор** - это структура данных, в которой хранятся метаданные файла и перечислены блоки с данными файла. Но начать надо с файловой системы. Файловые системы Ext используют блоки для хранения данных. По умолчанию размер одного блока равен 4092 байта. В начале раздела расположен суперблок, в котором находятся метаданные всей файловой системы, а за ним идут несколько зарезервированных блоков, а затем размещена таблица **Inode** и только после неё блоки с данными. Таким образом, все **Inode** размещены в начале раздела диска.


### Вопрос

Что находится в остальных столбцах? Прочитайте про файл `/proc/PID/maps` в `man procfs`.

### Вопрос

Прочитайте в `man mmap` ответы на следующие вопросы: Какие аргументы принимает `mmap`? В чём их смысл? Какой аргумент в каком регистре?

       The format of the call is as follows:
           pa=mmap(addr, len, prot, flags, fildes, off);


| Syscall | `sys_mmap` |
|--|--|
| `%rax` | `9` |
| `%rdi` | `unsigned long addr` |
| `%rsi` | `unsigned long len` |
| `%rdx` | `unsigned long prot` |
| `%r10` | `unsigned long flags` |
| `%r8` | `unsigned long fd` |
| `%r9` | `unsigned long offset` |

Функция `mmap` отражает **length** байтов, начиная со смещения **offset** файла (или другого объекта), определенного файловым дескриптором **fd**, в память, начиная с адреса **start**. Последний параметр (**адрес**) необязателен, и обычно бывает равен **0**. Настоящее местоположение отраженных данных возвращается самой функцией `mmap`, и *никогда не бывает равным 0*.
Аргумент **prot** описывает желаемый режим защиты памяти (он не должен конфликтовать с режимом открытия файла). Оно является либо `PROT_NONE` либо побитовым **ИЛИ** одного или нескольких флагов `PROT_*`.

- `PROT_EXEC` (данные в страницах могут исполняться);
- `PROT_READ` (данные можно читать);
- `PROT_WRITE` (в эту область можно записывать информацию);
- `PROT_NONE` (доступ к этой области памяти запрещен).

Параметр **flags** задает тип отражаемого объекта, опции отражения и указывает, принадлежат ли отраженные данные только этому процессу или их могут читать другие. Он состоит из комбинации следующих битов:

- `MAP_FIXED` Не использовать другой адрес, если адрес задан в параметрах функции. Если заданный адрес не может быть использован, то функция mmap вернет сообщение об ошибке. Если используется `MAP_FIXED`, то **start** должен быть пропорционален размеру страницы. Использование этой опции не рекомендуется.
- `MAP_SHARED` Разделить использование этого отражения с другими процессами, отражающими тот же объект. Запись информации в эту область памяти будет эквивалентна записи в файл. Файл может не обновляться до вызова функций `msync(2)` или `munmap(2)`.
- `MAP_PRIVATE` Создать неразделяемое отражение с механизмом **copy-on-write**. Запись в эту область памяти не влияет на файл. Не определено, являются или нет изменения в файле после вызова mmap видимыми в отраженном диапазоне.
Вы должны задать либо `MAP_SHARED`, либо `MAP_PRIVATE`.


## Задание 1

Создайте файл `hello.txt` с текстом `Hello, mmap!`. Используя заготовку, отобразите его в память и выведите текст из него в стандартный поток вывода. Не забудьте вызвать `munmap` (его номер системного вызова `11`) и закрыть файл (`close`, номер системного вызова `3`) по завершению работы с файлом.

- [hello_mmap.asm](./task1/hello_mmap.asm)

[task1](./task1/)

## Задание 2

Прочитайте документацию по системному вызову `fstat` (номер `5`). Вас будет интересовать поле `st_size` типа `off_t` структуры `struct stat`, которую функция `fstat` заполняет. Используйте его, чтобы корректно вычислить размер файла при выводе данных, и выведите их, используя функцию `print_substring` (принимает на вход два аргумента: адрес начала строки и количество байт для вывода). Полученный размер файла используйте в вызовах `mmap`, `munmap` и `print_substring`.

Для выполнения задания вам пригодиться следующая информация:

```c
sizeof(struct stat) = 144
offsetof(struct stat, st_size) = 48
sizeof(off_t) = 8
```

Для выделения памяти на хранение структуры `stat` рекомендуется использовать стек.

[task2](./task2/)

### Вопрос

Что делает функция `world`? Объясните принцип её работы.

[string.asm](./questions/string.asm)

## Задание 3

В этих файлах не хватает нескольких строчек чтобы можно было взаимодействовать с кодом друг друга. Допишите файлы так, чтобы функции `print_string` и `hello` вызывалась и проверьте результат. Подсказка: вспомните, что нужно, чтобы из одного файла с C-кодом вызвать код из другого файла.

[task3](./task3/)

### Вопрос

Какие аргументы может принимает функция языка С по умолчанию? В чем отличие прототипов `int f();` и `int f(void);`?

### Вопрос

Изучите `Makefile` [для сборки этой программы](./makefile). Что означают `$@`, `$^`?

- `$@` - цель (имя файла для сборки) текущего правила.
- `$^` - список всех зависимостей текущего правила, разделенных пробелами. Повторные включения зависимостей удаляются (каждая зависимость включается в список ровно один раз).

### Вопрос

Что такое секции `.rodata` и `.bss`?

### Вопрос

Что в `nasm` делают директивы `resb`, `resq` и [другие](https://nasm.us/doc/nasmdoc3.html)?

### Вопрос

В файле `hello.c`, в какой секции будет выделена строка `=”hello”=`?

### Вопрос

Введите `readelf` без аргументов. Прочитайте вывод и определите, какие ключи необходимы, чтобы отобразить три заголовка файла.

### Вопрос

Выведите `file header` (ключ `-h` или `--file-header`) для файла `hello.o`. Что такое `Entry point address` и почему его значение `0`?

### Вопрос

Выведите `program header` (ключ `-l` или `--program-headers`) для файла `hello.o`. Объясните результат.

### Вопрос

Определите адреса загрузки секций `.text` и `.rodata` используя список секций (ключ `-S` или `--section-headers`).

### Вопрос

Выведите `program header` для файла `hello`. В какие сегменты попадают `.text` и `.rodata`? Какие адреса загрузки этих сегментов?

### Вопрос

Модифицируйте программу на C так, чтобы она входила в бесконечный цикл. Это позволит нам проверить наши догадки про адреса секций и сегментов.

### Вопрос

Выведите карту регионов памяти для запущенной программы, с которой мы сегодня работаем. Сопоставьте сегменты, содержащие секции `.rodata` и `.text` регионам памяти. Верно ли, что одному региону памяти соответствует только один сегмент?

### Вопрос

Какие регионы соответствуют частям динамических библиотек? У динамических библиотек в Linux расширение `*.so`.

### Вопрос

Выполните `ldd hello`. Объясните результат.

### Вопрос

Как вы думаете, почему стандартная библиотека C реализована как динамическая библиотека, а не включается в состав исполняемого файла статически, как обычные `.o` файлы?

## Задание 4

Объедините ассемблерный код для вывода содержимого файла с кодом на языке C. Пусть ваша программа будет просить пользователя ввести название файла, а затем выведет его содержимое в стандартный поток вывода используя код, написанный в начале сегодняшнего семинара (сделайте из него функцию `print_file`, которая будет принимать имя файла первым аргументом). Не забудьте, что для корректной работы необходимо следовать соглашениям о вызовах и сохранить `callee-saved` регистры, которые вы используете, в начале своей функции `print_file`. Для вывода сообщений (например `“Please enter file name: “`) используйте собственную реализацию `print_string` из сегодняшнего семинара.
Желающие могут прочитать больше про регионы `[vdso]` и `[vvar]`, использующиеся для ускорения некоторых системных вызовов:

- `man vdso`
- [Implementing virtual system calls](https://lwn.net/Articles/615809/)

[task4](./task4/)
