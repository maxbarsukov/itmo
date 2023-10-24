; string.asm
global world
global print_string

section .rodata
message: db ", world!", 10, 0

section .text

string_length:
    mov  rax, 0
    .loop:
      xor  rax, rax
    .count:
      cmp  byte [rdi+rax], 0
      je   .end
      inc  rax
      jmp  .count
    .end:
      ret

world:
    mov rdi, message

print_string:
    push rax
    push rdi
    call string_length
    mov  rsi, [rsp]
    mov  rdx, rax
    mov  rax, 1
    mov  rdi, 1
    push rcx
    syscall
    pop  rcx
    pop  rdi
    pop  rax
    ret
