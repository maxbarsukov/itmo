section .data
message: db  'asdkbasdka', 0

section .text
global _start

exit:
    mov  rax, 60
    xor  rdi, rdi
    syscall

string_length:
  mov rax, 0
  .loop:
      xor rax, rax
  .count:
      cmp byte [rdi+rax], 0
      je .end
      inc rax
      jmp .count
  .end:
      ret

print_string:
    push rdi
    call string_length
    pop rdi
    mov rdx, rax
    mov rsi, rdi
    mov rdi, 1
    mov rax, 1
    syscall
    ret
