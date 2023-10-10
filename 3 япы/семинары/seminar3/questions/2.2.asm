%define i 1
     
%xdefine d i * 3
     
mov rax, d
     
; let's redefine i
%define i 100
mov rax, d
