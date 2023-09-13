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
