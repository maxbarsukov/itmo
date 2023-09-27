section .data
newline_char: db 10
codes:
    db      '0123456789ABCDEF', 10

section .text
global _start

_start:
    mov  rdi, 14
    call print_hex
    call print_new_line

    mov rdi, 15
    call print_hex
    call print_new_line

    mov rdi, 16
    call print_hex
    call print_new_line
    
    mov  rax, 60
    xor  rdi, rdi
    syscall

print_new_line:
    mov rax, 1
    mov rdi, 1
    mov rsi, newline_char
    mov rdx, 1
    syscall
    ret

print_hex:
    mov rax, rdi
    mov rdi, 1
    mov rdx, 1
    mov rcx, 64
    .loop:
        push rax
        sub  rcx, 4

        sar  rax, cl
        and  rax, 0xf

        lea  rsi, [codes + rax]
        mov  rax, 1


        push rcx
        syscall

        pop  rcx
        pop rax

        test rcx, rcx
        jnz .loop
    ret
