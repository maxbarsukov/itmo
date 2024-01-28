section .text

; ASCII symbols
%define SPACE_SYM   0x20
%define TAB_SYM     0x9
%define NEWLINE_SYM 0xA
%define NULL_SYM    0x0

; System calls
%define SYS_READ  0
%define SYS_WRITE 1
%define SYS_EXIT  60

; File descriptors
%define STDIN     0
%define STDOUT    1


;; Принимает код возврата и завершает текущий процесс
exit:
  mov rax, SYS_EXIT
  syscall


;; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:
  xor rax, rax  ; Clear the length counter

  .counter:
    ; Checking the current byte is 0
    cmp byte [rdi + rax], NULL_SYM
    je .return

    inc rax     ; Go to next byte
    jmp .counter
  .return:
    ret


;; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string:
  push rdi      ; Save rdi
  call string_length
  pop rdi       ; Restore rdi

  mov rdx, rax  ; rdx <- string length
  mov rsi, rdi  ; rsi <- string start address

  mov rax, SYS_WRITE
  mov rdi, STDOUT
  syscall

  ret


;; Переводит строку (выводит символ с кодом 0xA)
print_newline:
  mov rdi, NEWLINE_SYM


;; Принимает код символа и выводит его в stdout
print_char:
  push rdi      ; Save char

  mov rdx, 1    ; rdx <- string length == 1
  mov rsi, rsp  ; rsi <- symbol address from stack (see `push rdi`)

  mov rax, SYS_WRITE
  mov rdi, STDOUT
  syscall

  pop rdi       ; Restore char
  ret


;; Выводит знаковое 8-байтовое число в десятичном формате
print_int:
  cmp rdi, 0
  jge .greater_equals_zero

  ; rdi <= 0
  neg rdi         ; Now rdi is positive

  push rdi        ; Save int
  mov rdi, '-'
  call print_char ; Print '-' char
  pop rdi         ; Restore int
  .greater_equals_zero:


;; Выводит беззнаковое 8-байтовое число в десятичном формате
;; Совет: выделите место в стеке и храните там результаты деления
;; Не забудьте перевести цифры в их ASCII коды.
print_uint:
  mov rax, rdi    ; rax <- uint to print
  mov rcx, 10     ; rcx: divider

  mov r11, rsp    ; Save rsp to r11

  ; uint from rdi can be maximum 20 chars
  sub rsp, 32     ; Stack should be 16 bytes aligned for x64 ABI, so we need to allocate 32 bytes

  dec r11
  mov byte [r11], NULL_SYM  ; Null-terminator

  .loop:
    xor rdx, rdx  ; Clear division remainder for iter

    div rcx       ; rax /= 10; rdx = rax % 10
    add dl, '0'   ; rax % 10 to ASCII

    dec r11
    mov byte [r11], dl

    cmp rax, 0
    jnz .loop     ; if rax / 10 != 0

  mov rdi, r11    ; rdi <- uint string starts address
  call print_string
  add rsp, 32     ; Free used stack data
  ret


;; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
  ; rdi: points to string1 current byte
  ; rsi: points to string2 current byte
  xor rax, rax            ; rax: 0 (error) by default
  .loop:
    mov r11b, byte [rdi]  ; r11 low byte <- string1 current byte
    cmp r11b, byte [rsi]  ; Compare with string2 current byte
    jne .return

    ; string1 byte == string2 byte
    test r11b, r11b       ; Check for null-terminator
    jz .full_equal        ; Current byte is null-terminator

    inc rsi
    inc rdi
    jmp .loop             ; Check next byte

  .full_equal:
    inc rax
  .return:
    ret


;; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
  push 0          ; Element in stack for char

  mov rdx, 1
  mov rsi, rsp    ; Address for char (on stack)

  mov rax, SYS_READ
  mov rdi, STDIN
  syscall

  pop rax         ; rax <- char from stack
  ret


;; Принимает: адрес начала буфера, размер буфера
;; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
;; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
;; Останавливается и возвращает 0 если слово слишком большое для буфера
;; При успехе возвращает адрес буфера в rax, длину слова в rdx.
;; При неудаче возвращает 0 в rax
;; Эта функция должна дописывать к слову нуль-терминатор
read_word:
  push rdi      ; Save buffer address
  push r12      ; Save callee-saved r12
  mov r12, rdi

  push r13      ; Save callee-saved r13
  mov r13, rsi

  ; r12: current buffer address
  ; r13: current buffee size
  test r13, rsi
  jz .word_is_too_big

  .while_spaces:
    call read_char
    cmp rax, SPACE_SYM
    je .while_spaces
    cmp rax, TAB_SYM
    je .while_spaces
    cmp rax, NEWLINE_SYM
    je .while_spaces

  .read_loop:
    cmp rax, NULL_SYM     ; Null-terminator => full read
    je .full_read
    cmp rax, SPACE_SYM    ; Space => full read
    je .full_read
    cmp rax, TAB_SYM      ; Tab => full read
    je .full_read
    cmp rax, NEWLINE_SYM  ; Newline => full read
    je .full_read

    dec r13               ; Buffer size -= 1
    cmp r13, 0
    jbe .word_is_too_big  ; Current buffer size <= 0 => word is too big

    mov byte [r12], al    ; Save char to buffer
    inc r12               ; Current buffer address += 1
    call read_char

    jmp .read_loop

  .full_read:
    mov byte [r12], NULL_SYM  ; Add null-terminator to word
    pop r13
    pop r12

    mov rdi, [rsp]        ; Read rdi from stack without poping
    call string_length    ; rax <- word length
    mov rdx, rax          ; rdx <- rax == word length

    pop rax
    ret

  .word_is_too_big:
    pop r13
    pop r12
    pop rdi
    xor rax, rax          ; Error: rax <- 0
    ret


;; Принимает указатель на строку, пытается
;; прочитать из её начала беззнаковое число.
;; Возвращает в rax: число, rdx : его длину в символах
;; rdx = 0 если число прочитать не удалось
parse_uint:
  xor rax, rax    ; Clear result uint
  xor rdx, rdx    ; Clear uint length counter
  xor rcx, rcx    ; Clear buffer
  mov r11, 10     ; Factor

  .parse_loop:
    ; rcx low byte <- current symbol
    mov cl, byte [rdi+rdx]

    sub cl, '0'   ; ASCII to number
    jl .return
    cmp cl, 9     ; Check is valid number
    jg .return

    push rdx
    mul r11       ; rax *= 10
    pop rdx

    add rax, rcx  ; rax = rax * 10 + rcx

    inc rdx       ; Go to next byte
    jmp .parse_loop

  .return:
    ret


;; Принимает указатель на строку, пытается
;; прочитать из её начала знаковое число.
;; Если есть знак, пробелы между ним и числом не разрешены.
;; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был)
;; rdx = 0 если число прочитать не удалось
parse_int:
  cmp byte[rdi], '+'  ; First byte is '+'
  je .starts_with_sign

  cmp byte[rdi], '-'  ; First byte is not '-'
  jne parse_uint

  .starts_with_sign:
  push rdi            ; Save sign
  inc rdi             ; Go to next symbol after +/-
  call parse_uint
  pop rdi             ; Restore sign

  inc rdx             ; Sign => length + 1

  cmp byte[rdi], '+'
  je .return          ; '+' => return
  neg rax             ; '-' => rax = -rax

  .return:
  ret


;; Принимает указатель на строку, указатель на буфер и длину буфера
;; Копирует строку в буфер
;; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:
  ; rdi: string pointer
  ; rsi: buffer pointer
  ; rdx: buffer length
  xor rax, rax      ; Clear length counter
  .copy_loop:
    cmp rax, rdx
    jge .not_fits   ; counter >= buffer length => doesn't fits

    mov r11b, byte [rdi + rax] ; r11 low byte <- current byte
    mov byte [rsi + rax], r11b ; Paste byte

    test r11b, r11b
    jz .full_copied ; Current byte is null-terminator

    inc rax
    jmp .copy_loop  ; Go to next byte

  .not_fits:
    xor rax, rax
  .full_copied:
    ret
