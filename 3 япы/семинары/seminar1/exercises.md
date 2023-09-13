# Задания

## Задание 0.5

- Найти строчки строго из одного (любого) символа.

      grep '^.$' task0.5/test

- Найти строчки, в которых есть как минимум одна цифра.

      grep '[0-9]\+' task0.5/test

- Найти строчки, являющиеся числами в шестнадцатеричном формате C, например 0x1F, 0xABcd.

      grep '^0x[0-9a-fA-F]\+$ task0.5/test

- Найти строчки где есть слово из трёх букв.

      grep '\<[a-zA-Z]\{3\}\>' task0.5/test

- Найти пустые строчки.

      grep '^$' task0.5/test

- Инвертируйте результаты поиска grep с использованием ключей, т.е. выполните поиск строк, не удовлетворяющих шаблону.

      grep -v '^.$' task0.5/test


## Задание 1

Скомпилируйте первый файл `hello.asm` и создайте исполняемый файл `hello`:

    nasm -g hello.asm -felf64 -o hello.o
    ld -o hello hello.o
    ./hello

## Задание 2
Измените код программы `Hello, world!` так, чтобы она выводила два сообщения: одно в `stdout`, второе в `stderr`. Запустите программу перенаправив пото
к вывода `stdout` в файл `out.txt`, а `stderr` в файл `err.txt`.

    ./hello > out.txt 2> err.txt


```nasm
section .data
messageOut: db  'hello, stdout!', 10
messageErr: db  'hello, stderr!', 10

section .text
global _start

_start:
    mov     rax, 1           ; 'write' syscall number
    mov     rdi, 1           ; stdout descriptor
    mov     rsi, messageOut  ; string address
    mov     rdx, 15          ; string length in bytes
    syscall

    mov	  rax, 1
    mov   rdi, 2             ; stdout descriptor
    mov	  rsi, messageErr
    mov	  rdx, 15
    syscall

    mov     rax, 60          ; 'exit' syscall number
    xor     rdi, rdi
    syscall
```

## Задание 3

С помощью `gdb` запустите программу `Hello, world!`. Выполните её по шагам. Проследите за изменениями регистров `rip` на протяжении работы программы. Что оно означает? Также обратите внимание на значение `rax` после системного вызова `write`.


| Положение | Команда | `rax` | `rip` | Комментарий
| -- | -- | -- | -- | -- |
| `0x401000 <_start>` | `mov eax,0x1` | `1` | `0x401000` | |
| `0x401005 <_start+5>` | `mov edi,0x1` | `1` | `0x401005` | |
| `0x40100a <_start+10>` | `movabs rsi,0x402000` | `1` | `0x40100a` | |
| `0x401014 <_start+20>` | `mov edx,0xe` | `1` | `0x401014` | |
| `0x401019 <_start+25>` | `syscall` | `1` | `0x401019` | |
| `0x40101b <_start+27>` | `mov eax,0x3c` | `0xe` | `0x40101b` | Вывод `hello, world!` |
| `0x401020 <_start+32>` | `xor rdi,rdi` | `0x3c` | `0x401020` | |
| `0x401023 <_start+35>` | `syscall` | `0x3c` | `0x401023` | 3авершение процесса |

## Задание 3.5

Попробуйте при отладке программы пройти назад, а не вперед.

      break _start
      layout asm
      layout regs
      run
      record
      si
      si
      rsi   ; <---- шаг назад
      rsi
      si
      ...

## Задание 4

Пройдите по шагам следующую программу и проследите за изменением значения регистра `rax`.

| Положение | `rax` hex | `rax` dem |
| -- | -- | -- |
| `0x401000 <_start>` | `0` | 0 |
| `0x401007 <_start+7>` | `0xffffffffffffffff` | -1 |
| `0x401009 <_start+9>` | `0xffffffffffffff00` | -256 |
| `0x40100d <_start+13>` | `0xffffffffffff0000` | -65536 |
| `0x401012 <_start+18>` | `0x0` | 0 |
| `0x401017 <_start+23>` | `0x3c` | 60 |
| `0x40101a <_start+26>` | `0x3c` | 60 |
