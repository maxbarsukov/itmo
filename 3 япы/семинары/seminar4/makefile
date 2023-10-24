CFLAGS     = -g -O2 -Wall -Werror -std=c17 -Wdiscarded-qualifiers -Wincompatible-pointer-types -Wint-conversion
CC         = gcc
LD         = gcc
ASM        = nasm
ASMFLAGS   =-felf64 -g

# Если есть код на C, то компоновку тоже нужно производить
# с помощью gcc, а не ld
hello: hello.o string.o
	$(LD) -o $@ $^

# target : dependencies
#     do this
hello.o: hello.c 
	$(CC) -c $(CFLAGS) -o $@ $<

string.o: string.asm
	$(ASM) $(ASMFLAGS) -o $@ $<

clean: 
	$(RM) hello hello.o string.o

.PHONY: clean
