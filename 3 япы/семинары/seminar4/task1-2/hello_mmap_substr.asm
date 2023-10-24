; hello_mmap.asm
%define O_RDONLY 0
%define PROT_READ 0x1
%define MAP_PRIVATE 0x2
%define SYS_WRITE 1
%define SYS_OPEN 2
%define SYS_MMAP 9
%define SYS_EXIT 60
%define FD_STDOUT 1

section .data
    ; This is the file name. You are free to change it.
    fname: db 'hello.txt', 0

section .text
global _start

; use exit system call to shut down correctly
exit:
    mov  rax, SYS_EXIT
    xor  rdi, rdi
    syscall

; These functions are used to print a null terminated string
; rdi holds a string pointer
print_string:
    push rdi
    call string_length
    pop  rsi
    mov  rdx, rax
    mov  rax, SYS_WRITE
    mov  rdi, FD_STDOUT
    syscall
    ret

string_length:
    xor  rax, rax
.loop:
    cmp  byte [rdi+rax], 0
    je   .end
    inc  rax
    jmp .loop
.end:
    ret

; This function is used to print a substring with given length
; rdi holds a string pointer
; rsi holds a substring length
print_substring:
    mov  rdx, rsi 
    mov  rsi, rdi
    mov  rax, SYS_WRITE
    mov  rdi, FD_STDOUT
    syscall
    ret

_start:
    ; Вызовите open и откройте fname в режиме read only.
    mov  rax, SYS_OPEN
    mov  rdi, fname
    mov  rsi, O_RDONLY    ; Open file read only
    mov  rdx, 0 	      ; We are not creating a file
                          ; so this argument has no meaning
    syscall
    ; rax holds the opened file descriptor now

    ; Вызовите mmap c правильными аргументами
    ; Дайте операционной системе самой выбрать, куда отобразить файл
    ; Размер области возьмите в размер страницы
    ; Область не должна быть общей для нескольких процессов
    ; и должна выделяться только для чтения.

    mov r8, rax
    push r8
    mov r9, 0
    mov r10, MAP_PRIVATE
    mov rax, SYS_MMAP
    mov rdi, 0
    mov rsi, 4096
    mov rdx, PROT_READ
    syscall

    push rax
    sub rsp, 144
    mov r15, rax

    mov rax, 5
    mov rsi, rsp
    mov rdi, r8
    syscall

    mov rdi, r15
    mov rsi, [rsp + 48]
    push rdi
    call print_substring

    mov rax, 11
    pop rdi
    mov rsi, 4096

    syscall

    mov rax, 3
    pop rdi
    syscall

    call exit
