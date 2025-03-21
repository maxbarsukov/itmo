; local_vars.asm
section .data

newline_char:   db 10
codes:          db '0123456789ABCDEF', 10

section .text
global _start

print_newline:
    mov rax, 1
    mov rdi, 1
    mov rsi, newline_char
    mov rdx, 1
    syscall
    ret
    
exit:
    mov  rax, 60
    xor  rdi, rdi
    syscall

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
    
_start:
    sub     rsp, 24
    mov     byte [rsp+8],   0xAA
    mov     byte [rsp+16],  0xBB
    mov     byte [rsp+24],  0xFF

    mov     rdi, [rsp+8]
    call    print_hex
    call    print_newline
    add     rsp, 8

    mov     rdi, [rsp+8]
    call    print_hex
    call    print_newline
    add     rsp, 8

    mov     rdi, [rsp+8]
    call    print_hex
    call    print_newline
    add     rsp, 8

    call    exit
