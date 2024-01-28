%include "src/lib.inc"

%define QWORD 8

section .text

global find_word

;; Принимает два аргумента:
;; указатель на нуль-терминированную строку и указатель на начало словаря.
;; Проходит по всему словарю в поисках подходящего ключа.
;; Если подходящее вхождение найдено, вернёт адрес начала вхождения в словарь, иначе 0.
find_word:
  ; @arg rdi: string address - not changes in find_word
  ; @arg rsi: dict start address
  ; @var rcx: address of current dict element

  push rsi            ; Save rsi
  mov rcx, rsi

  .find_loop:
    lea  rsi, [rcx + QWORD] ; Skip next element address, rsi <- address of value
    call string_equals

    test rax, rax     ; If strings equal
    jnz  .found       ; Then we found needed string

    mov  rcx, [rcx]   ; Go to next element address, rcx <- next elem
    test rcx, rcx     ; If the end of dict
    jz   .not_found   ; Dict is over, no such string

    jmp  .find_loop   ; Try next dict element

  .not_found:
    xor  rax, rax      ; Nullify rax
    jmp  .return
  .found:
    mov  rax, rsi      ; Return address of value in dict
  .return:
    pop  rsi           ; Restore rsi
    ret
