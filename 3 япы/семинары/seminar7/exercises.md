# Задания

### Вопрос

Как посмотреть флаги компиляции в [godbolt](http://godbolt.org)?

      Dropdown Menu > Detailed Compiler Flags

или Compiler options... > Нажать на зеленую галочку.

В верхнем левом углу.

### Вопрос

Что означают флаги: `-O0`, `-O1`, `-O2`, `-Os`?

Опции, которые начинаются с O, - это указания компилятору включать различные оптимизации.

- `-O0` (ноль) - это самые простые и примитивные оптимизации;
- `-O1` - более сильные оптимизации;
- `-O2` - оптимизировать все, что можно, но только проверенные и надежные оптимизации;
- `-O3` - жесткая и насильная оптимизация, применяются экспериментальные методы.

Каждый последующий уровень включает предыдущие. Что происходит на каждом уровне, сильно зависит от версии компилятора и нужно смотреть в документацию (`man gcc`).

Также есть ещё `-Os` - оптимизировать по размеру.


### Вопрос

Скомпилируйте [программу](https://gitlab.se.ifmo.ru/programming-languages/cse-programming-languages-fall-2022/main/-/blob/master/seminar-5/printer.c) `printer.c` из семинара 5 с ключами `-O0` и `-O3` (в `Godbolt` можно создать две вкладки с компиляторами рядом и сравнить листинги).

Листинги отличаются.

Например, с `-O0`:

```nasm
print_lit:
   push    rbp
   mov     rbp, rsp
   sub     rsp, 16
   mov     QWORD PTR [rbp-8], rdi
   mov     QWORD PTR [rbp-16], rsi
   mov     rax, QWORD PTR [rbp-16]
   mov     rdx, QWORD PTR [rax+8]
   mov     rax, QWORD PTR [rbp-8]
   mov     esi, OFFSET FLAT:.LC4
   mov     rdi, rax
   mov     eax, 0
   call    fprintf
   nop
   leave
   ret
```

С `-O3`:

```nasm
print_lit:
   mov     rdx, QWORD PTR [rsi+8]
   xor     eax, eax
   mov     esi, OFFSET FLAT:.LC0
   jmp     fprintf
```

Можно увидеть некоторые оптимизации: `jmp fprintf` вместо `call` + `nop` + `leave` + `ret` и `xor eax, eax` вместо `mov eax, 0`

### Вопрос

Посмотрите как с увеличением уровня оптимизаций пропадают чтения из памяти:

```c
/* volatile.c */
#include <stdio.h>

void print_int(int x) { printf("%d", x); }

int main() {

  int x = 42;
  volatile int y = 42;

  print_int(x);
  print_int(y);
}
```

<table>
<tr>
<td>

`-O0`
</td><td>

`-O3`
</td>
</tr>
<tr>
<td>

```nasm
.LC0:
   .string "%d"
print_int:
   push    rbp
   mov     rbp, rsp
   sub     rsp, 16
   mov     DWORD PTR [rbp-4], edi
   mov     eax, DWORD PTR [rbp-4]
   mov     esi, eax
   mov     edi, OFFSET FLAT:.LC0
   mov     eax, 0
   call    printf
   nop
   leave
   ret
main:
   push    rbp
   mov     rbp, rsp
   sub     rsp, 16
   mov     DWORD PTR [rbp-4], 42
   mov     DWORD PTR [rbp-8], 42
   mov     eax, DWORD PTR [rbp-4]
   mov     edi, eax
   call    print_int
   mov     eax, DWORD PTR [rbp-8]
   mov     edi, eax
   call    print_int
   mov     eax, 0
   leave
   ret
```

</td>
<td>


```nasm
.LC0:
   .string "%d"
print_int:
   mov     esi, edi
   xor     eax, eax
   mov     edi, OFFSET FLAT:.LC0
   jmp     printf
main:
   sub     rsp, 24
   mov     esi, 42
   mov     edi, OFFSET FLAT:.LC0
   xor     eax, eax
   mov     DWORD PTR [rsp+12], 42
   call    printf
   mov     esi, DWORD PTR [rsp+12]
   mov     edi, OFFSET FLAT:.LC0
   xor     eax, eax
   call    printf
   xor     eax, eax
   add     rsp, 24
   ret
```

</td>
</tr>
</table>

Конструкции вида `QWORD PTR` читают из памяти.

Скомпилировав код с ключами `-O0` и `-O3` мы явно увидим разницу в количестве этих конструкций, что указывает на меньшее количество выделений и чтений памяти.


### Вопрос

Пометьте функцию `print_int` как `static`. Что произошло в оптимизированном коде и почему?

В неоптимизированном коде ничего не изменилось.

В оптимизированном коде исчезла метка `print_int`, а в `main` появилось два вызова `call printf`, т.е. подпрограмма была *встроена* (inlined) в вызывающий ее код, что избавило процессор от необходимости прыгать в ячейку, по адресу которой начинается эта функция.

### Вопрос

Иногда используется также специализированные инструкции `enter` и `leave`.

Что делает эта инструкция?

`enter` создаёт stack frame, а `leave` уничтожает его. С параметрами `0,0` в `enter`, они эквивалентны данному коду:

```nasm
; enter
push rbp
mov rbp, rsp
sub rsp, <сколько выделить байтов в стеке>

; leave
mov rsp, rbp  ; отмотать стек в исходное состояние
pop rbp       ; восстановить rbp
```

Первый параметр `enter` указывает объем пространства, выделяемого для локальных переменных. Например, `enter 5, 0` - это примерно эквивалентно:

```nasm
push rbp
mov rbp, rsp
sub rsp, 5
```

Таким образом `enter` и `leave` упрощают написание пролога и эпилога функции.

Обычно компиляторы генерируют код, который напрямую манипулирует указателями кадров стека, поскольку `enter` немного медленнее по сравнению с `mov`/`sub`.

### Вопрос

Рассмотрим следующий файл:

```c
/* prologue-epilogue.c */

int maximum(int a, int b) {
  char buffer[4096];
  buffer[0] = 0x7;
  if (a < b)
    return b;
  return a;
}

int main(void) {
  int x = maximum(42, 999);
  return 0;
}
```

Скомпилируйте его без оптимизаций и объясните содержимое функции `maximum`. Почему `rsp` уменьшается на это число?

```nasm
maximum:
   push    rbp
   mov     rbp, rsp
   sub     rsp, 3992
   mov     DWORD PTR [rbp-4100], edi
   mov     DWORD PTR [rbp-4104], esi
   mov     BYTE PTR [rbp-4096], 7
   mov     eax, DWORD PTR [rbp-4100]
   cmp     eax, DWORD PTR [rbp-4104]
   jge     .L2
   mov     eax, DWORD PTR [rbp-4104]
   jmp     .L4
.L2:
   mov     eax, DWORD PTR [rbp-4100]
.L4:
   leave
   ret
main:
   push    rbp
   mov     rbp, rsp
   sub     rsp, 16
   mov     esi, 999
   mov     edi, 42
   call    maximum
   mov     DWORD PTR [rbp-4], eax
   mov     eax, 0
   leave
   ret
```

`rsp` уменьшается на `3992`, потому что сначала элементы массива записывается в красную зону функции, а потом уже дальше.

`128` (красная зона) + `3992` (размер массива) - `24` (2 параметра + локальная переменная) = `4096`

### Вопрос

Вспомните, почему пару инструкций call + ret можно заменить на jmp, например:

```nasm
...
call f
ret


f:
...
ret

; то же, что и:
...
jmp f


f:
...
ret
```

Когда последней инструкцией в функции является вызов другой, функции, можно сразу перейти к ней, не задействуя стек.

В нехвостовой рекурсии нам требуется хранить в стеке локальные переменные *предыдущего вызова*, и при очередном рекурсивном вызове может произойти **stack-overflow**, но при хвостовой эти значения хранить не нужно и стек не меняется, т.е. **глубина рекурсии ничем не ограничена**.

## Задание 1

Скомпилируйте и запустите следующий код: [tail-rec.c](https://gitlab.se.ifmo.ru/programming-languages/cse-programming-languages-fall-2023/main/-/blob/master/seminar-7/tail-rec.c)

Что выведется на экран? Объясните это поведение.

Как можно переписать функцию (какую?) чтобы программа корректно считала длину длинного списка?

[task1](./task1/)

Программа падает с `segmentation fault`, вызванным переполнением стека.

Это происходит из-за рекурсивной функции `size_t list_length(struct list const *l)` с глубиной в размер списка (_в примере 1024*1024_). Значит, нужно переписать эту функцию на циклы или хвостовую рекурсию.

Перепишем с хвостовой рекурсией: [fixed-tail-rec.c](./task1/fixed-tail-rec.c)

Запускаем:

    gcc -std=c99 -O2 task1/fixed-tail-rec.c
    ./a.out

Программа выводит `1048576` = 1024*1024 - длину списка, то есть работает корректно.

### Вопрос

Как в функцию передаются следующие аргументы после шестого?

Седьмой и следующий аргументы передаются через стеке.

```c
int func(int a, int b, int c, int d, int e, int f, int g, int h) {
    return f+g+h;
}
```

```nasm
func:
   push    rbp
   mov     rbp, rsp
   mov     DWORD PTR [rbp-4], edi
   mov     DWORD PTR [rbp-8], esi
   mov     DWORD PTR [rbp-12], edx
   mov     DWORD PTR [rbp-16], ecx
   mov     DWORD PTR [rbp-20], r8d
   mov     DWORD PTR [rbp-24], r9d
   mov     edx, DWORD PTR [rbp-24]
   mov     eax, DWORD PTR [rbp+16]
   add     edx, eax
   mov     eax, DWORD PTR [rbp+24]
   add     eax, edx
   pop     rbp
   ret
```

### Вопрос

Что такое `ASLR` (address space layout randomization)?
Отключите `ASLR` следующей командой:

    echo 0 | sudo tee /proc/sys/kernel/randomize_va_space

`ASLR` (англ. **address space layout randomization** — **«рандомизация размещения адресного пространства**») — технология, применяемая в операционных системах, при использовании которой случайным образом изменяется расположение в адресном пространстве процесса важных структур данных, а именно образов исполняемого файла, подгружаемых библиотек, кучи и стека.

Технология `ASLR` создана для *усложнения эксплуатации нескольких типов уязвимостей*. Например, если при помощи **переполнения буфера** или другим методом атакующий получит возможность передать управление по произвольному адресу, ему **нужно будет угадать**, по какому именно адресу расположен *стек*, *куча* или *другие структуры данных*, в которые можно поместить шелл-код.

## Задание 2

Попробуйте переписать адрес возврата так, чтобы вместо возвращения из `vulnerable` в `main` запустить функцию `print_users`.

Программа может аварийно завершиться, главное – чтобы функция отработала и вывела на экран список пользователей и их паролей.

![task2](https://gitlab.se.ifmo.ru/programming-languages/cse-programming-languages-fall-2023/main/-/raw/master/seminar-7/img/output.png)

Вы не можете переписывать программу, можете только подавать ей на вход разные данные.
Вы **можете** изучать скомпилированный файл с помощью `gdb`, запускать его, смотреть содержимое памяти.
Также можно пользоваться `objdump` или `readelf`, `nm` и любыми иными средствами для узнавания адреса `print_users`.
Не забывайте, что он может меняться после каждой перекомпиляции!

Также не забудьте, что в памяти многобайтовые числа, в том числе адреса, хранятся в соответствии с `Little Endian`.

[task2](./task2/)


## Задание 3

Исправьте уязвимость.

Ограничиваем `fill` размером буфера, прекращаем чтение при `read_total >= buf_size`.

Вводим ту же строку, получаем `nothing happened`.
Уязвимость исправлена!

[task3](./task3/)


## Задание 4

Мы читаем пароль используя функцию `fscanf` и спецификатор `%s` в буфер `buffer` никак не ограничивая количество читаемых символов.

Далее прочитанный пароль сравнивается с сохраненным и устанавливается флаг `okay`. При переполнении буффера может произойти замена значаения флага и пароль будет считаться введенным верно.

Исправьте данный код так, чтобы исключить потенциальную уязвимость.

[task4](./task4/)

Уязвимость при вводе:

    $ echo -n -e "\x72\x72\x72\x72\x72\x72\x72\x72\x72\x72\x1" | ./check-pwd

    Access granted.
