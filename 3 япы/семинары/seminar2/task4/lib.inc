; print_string.asm 
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
  mov  rdx, rax
  mov  rsi, rdi
  mov  rax, 1
  mov  rdi, 1
  syscall
  ret

parse_uint:
  push rbx
  xor rdx, rdx
  xor rax, rax
  xor rbx, rbx
  .loop:
    mov bl, byte [rdi + rdx]

    sub bl, '0'
    jl .return

    cmp bl, 9
    jg .return

    push rdx
    mov rdx, 10
    mul rdx       ; rax *= 10
    pop rdx

    add rax, rbx  ; rax += rbx

    inc rdx
    jmp .loop

  .return:
    pop rbx
    ret
