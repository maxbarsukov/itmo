%include "src/lib.inc"
%include "src/words.inc"
%include "src/dict.inc"

%define MAX_LENGTH 255

section .rodata
  too_long_msg:  db "error: The string is too long", 0
  not_found_msg: db "error: Not Found", 0

section .bss
  input: resb MAX_LENGTH + 1


section .text

global _start

_start:
  mov rdi, input
  mov rsi, MAX_LENGTH
  call read_string    ; Read user input

  test rax, rax
  mov rdi, too_long_msg
  jz .error           ; If user's string is too big

  mov rdi, rax
  mov rsi, first_word ; Dict start address
  call find_word

  test rax, rax
  mov rdi, not_found_msg
  jz .error

  mov rdi, rax        ; rdi <- address of dict key

  push rdi
  call string_length  ; Skip dict key
  pop rdi

  add rdi, rax        ; Go to dict value
  inc rdi             ; Skip null-terminator

  call print_string
  call print_newline

  xor rdi, rdi
  jmp exit

  .error:
    call print_error
    call print_newline
    mov rdi, 1
    jmp exit
