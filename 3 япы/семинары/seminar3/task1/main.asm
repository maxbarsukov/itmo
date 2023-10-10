%macro concat 1-*
    %1: 
        %rotate 1
        %rep %0-2
            db %1, ", "
            %rotate 1
        %endrep
        db %1, 0
    %2_end:
%endmacro

section .data

concat string, "hello", "another", "world"

section .text

%define NULL_SYM  0x0

; System calls
%define SYS_WRITE 1
%define SYS_EXIT  60

; File descriptors
%define STDOUT    1

global _start
_start:
   mov rdi, string
   call print_string
   xor rdi, rdi
   call exit


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
