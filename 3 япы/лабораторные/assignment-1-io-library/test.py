#!/usr/bin/python3

__unittest = True

import subprocess
import re
import unittest
import xmlrunner
from subprocess import CalledProcessError, Popen, PIPE

#-------helpers---------------

def starts_uint( s ):
    matches = re.findall('^\d+', s)
    if matches:
        return (int(matches[0]), len(matches[0]))
    else:
        return (0, 0)

def starts_int( s ):
    matches = re.findall('^-?\d+', s)
    if matches:
        return (int(matches[0]), len(matches[0]))
    else:
        return (0, 0)

def unsigned_reinterpret(x):
    if x < 0:
        return x + 2**64
    else:
        return x

def first_or_empty( s ):
    sp = s.split()
    if sp == [] :
        return ''
    else:
        return sp[0]

#-----------------------------

before_all="""
%macro call 1
mov rax, -1
push rbx
push rbp
push r12 
push r13 
push r14 
push r15 
call %1
cmp r15, [rsp] 
jne convention_error
pop r15
cmp r14, [rsp] 
jne convention_error
pop r14
cmp r13, [rsp] 
jne convention_error
pop r13
cmp r12, [rsp] 
jne convention_error
pop r12
cmp rbp, [rsp] 
jne convention_error
pop rbp
cmp rbx, [rsp] 
jne convention_error
pop rbx
mov rdi, -1
mov rsi, -1
mov rcx, -1
mov r8, -1
mov r9, -1
mov r10, -1
mov r11, -1
%endmacro

%include "lib.inc"

global _start

section .text
convention_error:
    mov rax, 1
    mov rdi, 2
    mov rsi, err_calling_convention
    mov rdx, err_calling_convention.end - err_calling_convention
    syscall
    mov rax, 60
    mov rdi, -41
    syscall

section .data
    err_calling_convention: db "You did not respect the calling convention! Check that you handled caller-saved and callee-saved registers correctly", 10
    .end:
"""

class IOLibraryTest(unittest.TestCase):
    def compile(self, fname, text):
        f = open( fname + '.asm', 'w')
        f.write( text )
        f.close()

        self.assertEqual(subprocess.call( ['nasm', '-f', 'elf64', fname + '.asm', '-o', fname+'.o'] ), 0, 'failed to compile')
        self.assertEqual(subprocess.call( ['ld', '-o' , fname, fname+'.o'] ), 0, 'failed to link')

    def launch(self, fname, input):
        output = b''
        try:
            p = Popen(['./'+fname], shell=None, stdin=PIPE, stdout=PIPE)
            (output, _) = p.communicate(input.encode())
            self.assertNotEqual(p.returncode, -11, 'segmentation fault')
            return (output.decode(), p.returncode)
        except CalledProcessError as exc:
            self.assertNotEqual(exc.returncode, -11, 'segmentation fault')
            return (exc.output.decode(), exc.returncode)

    def perform(self, fname, text, input):
        self.compile(fname, before_all + text)
        return self.launch(fname, input)



    def test_string_length(self):
        inputs = ['asdkbasdka', 'qwe qweqe qe', '']
        for input in inputs:
            text = """
section .data
str: db '""" + input + """', 0

section .text
_start:
    mov rdi, str
    call string_length
    mov rdi, rax
    mov rax, 60
    syscall
"""
            (output, code) = self.perform('string_length', text, input)
            self.assertEqual(code, len(input), 'string_length(%s) returned wrong length: %d' % (repr(input), code))



    def test_print_string(self):
        inputs = ['ashdb asdhabs dahb', ' ', '']
        for input in inputs:
            text = """
section .data
str: db '""" + input + """', 0

section .text
_start:
    mov rdi, str
    call print_string
    xor rdi, rdi
    mov rax, 60
    syscall
"""
            (output, code) = self.perform('print_string', text, input)
            self.assertEqual(output, input, 'print_string(%s) printed wrong string: %s' % (repr(input), repr(output)))



    def test_string_copy(self):
        inputs = ['ashdb asdhabs dahb', ' ', '']
        for input in inputs:
            text = """
section .data
arg1: db '""" + input + """', 0
arg2: times """ + str(len(input) + 1) + """ db  66

section .text
_start:
    mov rdi, arg1
    mov rsi, arg2
    mov rdx, """ + str(len(input) + 1) + """
    call string_copy

    mov rdi, arg2 
    call print_string
    mov rax, 60
    xor rdi, rdi
    syscall
"""
            (output, code) = self.perform('string_copy', text, input)
            self.assertEqual(output, input, 'string_copy(%s) put wrong string into buffer: %s' % (repr(input), repr(output)))



    def test_string_copy_too_long(self):
        inputs = ['ashdb asdhabs dahb', ' ', '']
        for input in inputs:
            text = """
section .rodata
err_too_long_msg: db "string is too long", 10, 0

section .data
arg1: db '""" + input + """', 0
arg2: times """ + str(len(input)//2) + """ db  66

section .text
_start:
    mov rdi, arg1
    mov rsi, arg2
    mov rdx, """ + str(len(input)//2) + """
    call string_copy
    test rax, rax
    jnz .good
    mov rdi, err_too_long_msg 
    call print_string
    jmp _exit
    .good:
    mov rdi, arg2 
    call print_string
_exit:
    mov rax, 60
    xor rdi, rdi
    syscall
"""
            (output, code) = self.perform('string_copy_too_long', text, input)
            self.assertNotEqual(output.find('too long'), -1, 'string_copy(%s) should have failed, but returned: %s' % (repr(input), repr(output)))



    def test_print_char(self):
        inputs = ['a', ' ', 'c']
        for input in inputs:
            text = """
section .text
_start:
    mov rdi, '""" + input + """'
    call print_char
    mov rax, 60
    xor rdi, rdi
    syscall
"""
            (output, code) = self.perform('print_char', text, input)
            self.assertEqual(output, input, 'print_char(%s) printed wrong char: %s' % (repr(input), repr(output)))



    def test_print_uint(self):
        inputs = ['-1', '12345234121', '0', '12312312', '123123']
        for input in inputs:
            text = """
section .text
_start:
    mov rdi, """ + input + """
    call print_uint
    mov rax, 60
    xor rdi, rdi
    syscall
"""
            (output, code) = self.perform('print_uint', text, input)
            uinput = str(unsigned_reinterpret(int(input)))
            self.assertEqual(output, uinput, 'print_uint(%s) printed wrong number: %s, expected: %s' % (repr(input), repr(output), repr(uinput)))



    def test_print_int(self):
        inputs = ['-1', '-12345234121', '0', '123412312', '123123']
        for input in inputs:
            text = """
section .text
_start:
    mov rdi, """ + input + """
    call print_int
    mov rax, 60
    xor rdi, rdi
    syscall
"""
            (output, code) = self.perform('print_int', text, input)
            self.assertEqual(output, input, 'print_int(%s) printed wrong number: %s' % (repr(input), repr(output)))



    def test_read_char(self):
        inputs = ['-1', '-1234asdasd5234121', '', '   ', '\t   ', 'hey ya ye ya', 'hello world' ]
        for input in inputs:
            text = """
section .text
_start:
    call read_char
    mov rdi, rax
    mov rax, 60
    syscall
"""
            (output, code) = self.perform('read_char', text, input)
            if input == "":
                self.assertEqual(code, 0, 'read_char with empty input should return 0')
            else:
                self.assertEqual(code, ord(input[0]), 'read_char(%d) returned incorrect char: %d' % (ord(input[0]), code))



    def test_read_word(self):
        inputs = ['-1'] # , '-1234asdasd5234121', '', '   ', '\t   ', 'hey ya ye ya', 'hello world' ],

        for input in inputs:
            text = """
section .data
word_buf: times 20 db 0xca

section .text
_start:
    mov rdi, word_buf
    mov rsi, 20 
    call read_word
    mov rdi, rax
    call print_string

    mov rax, 60
    xor rdi, rdi
    syscall
"""
            (output, code) = self.perform('read_word', text, input)
            input_word = first_or_empty(input)
            self.assertEqual(output, input_word, 'read_word(%s) put incorrect word in the buffer: %s, expected: %s' % (repr(input), repr(output), repr(input_word)))



    def test_read_word_length(self):
        inputs = ['-1', '-1234asdasd5234121', '', '   ', '\t   ', '\t   123', 'hey ya ye ya', 'hello world' ]
        for input in inputs:
            text = """
section .data
word_buf: times 20 db 0xca

section .text
_start:
    mov rdi, word_buf
    mov rsi, 20 
    call read_word
    mov rax, 60
    mov rdi, rdx
    syscall
"""
            (output, code) = self.perform('read_word_length', text, input)
            input_word = first_or_empty(input)
            self.assertEqual(code, len(input_word), 'read_word(%s) returned incorrect length: %d, expected: %d' % (repr(input), code, len(input_word)))



    def test_read_word_too_long(self):
        inputs = [ 'asdbaskdbaksvbaskvhbashvbasdasdads wewe', 'short' ]
        for input in inputs:
            text = """
section .data
stub: times 5 db 0xca
word_buf: times 20 db 0xca

section .text
_start:
    mov rdi, word_buf
    mov rsi, 20 
    call read_word
    mov rdi, rax
    mov rax, 60
    syscall
"""
            (output, code) = self.perform('read_word_too_long', text, input)
            input_word = first_or_empty(input)
            if len(input_word) > 19:
                self.assertEqual(code, 0, 'read_word(%s) overflows buffer, but does not fail' % repr(input))
            else:
                self.assertNotEqual(code, 0, 'read_word(%s) does not overflow buffer, but fails' % repr(input))



    def test_parse_uint(self):
        inputs = ["0", "1234567890987654321hehehey", "1" ]
        for input in inputs:
            text = """
section .data
input: db '""" + input  + """', 0

section .text
_start:
    mov rdi, input
    call parse_uint
    push rdx
    mov rdi, rax
    call print_uint
    mov rax, 60
    pop rdi
    syscall
"""
            (output, code) = self.perform('parse_uint', text, input)
            (input_num, input_len) = starts_uint(input)

            self.assertEqual(output, str(input_num), 'parse_uint(%s) parsed wrong number: %s, expected: %s' % (repr(input), repr(output), repr(str(input_num))))
            self.assertEqual(code, input_len, 'parse_uint(%s) returned wrong length: %d, expected: %d' % (repr(input), code, input_len))



    def test_parse_int(self):
        inputs = ["0", "1234567890987654321hehehey", "-1dasda", "-eedea", "-123123123", "1" ]
        for input in inputs:
            text = """
section .data
input: db '""" + input  + """', 0

section .text
_start:
    mov rdi, input
    call parse_int
    push rdx
    mov rdi, rax
    call print_int
    mov rax, 60
    pop rdi
    syscall
"""
            (output, code) = self.perform('parse_int', text, input)
            (input_num, input_len) = starts_int(input)

            if input_len == 0:
                self.assertEqual(output, '0', 'parse_int(%s) should have failed, but parsed %s' % (repr(input), output))
            else:
                self.assertEqual(output, str(input_num), 'parse_int(%s) parsed wrong number: %s, expected: %s' % (repr(input), repr(output), repr(str(input_num))))
                self.assertEqual(code, input_len, 'parse_int(%s) returned wrong length: %d, expected: %d' % (repr(input), code, input_len))



    def test_string_equals(self):
        inputs = ['ashdb asdhabs dahb', ' ', '', "asd" ]
        for input in inputs:
            text = """
section .data
str1: db '""" + input + """',0
str2: db '""" + input + """',0

section .text
_start:
    mov rdi, str1
    mov rsi, str2
    call string_equals
    mov rdi, rax
    mov rax, 60
    syscall
"""
            (output, code) = self.perform('string_equals', text, input)
            self.assertEqual(code, 1, 'string_equals(%s, %s) should return 1' % (repr(input), repr(input)))



    def test_string_not_equals(self):
        inputs = ['ashdb asdhabs dahb', ' ', '', "asd" ]
        for input in inputs:
            text = """
section .data
str1: db '""" + input + """',0
str2: db '""" + input + """!!',0

section .text
_start:
    mov rdi, str1
    mov rsi, str2
    call string_equals
    mov rdi, rax
    mov rax, 60
    syscall
"""
            (output, code) = self.perform('string_not_equals', text, input)
            self.assertEqual(code, 0, 'string_equals(%s, %s!!) should return 0' % (repr(input), repr(input)))



if __name__ == "__main__":
    with open('report.xml', 'w') as report:
        unittest.main(testRunner=xmlrunner.XMLTestRunner(output=report), failfast=False, buffer=False, catchbreak=False)
