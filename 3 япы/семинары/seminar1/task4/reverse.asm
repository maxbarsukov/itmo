section .text
global _start

_start:
    mov     rax, 0FFFFFFFFFFFFFFFFh
    mov     al, 0
    mov     ax, 0
    mov     eax, 0

    mov     rax, 60
    xor     rdi, rdi
    syscall
