CFLAGS     = -g -O2 -Wall -Werror -std=c17 -Wdiscarded-qualifiers -Wincompatible-pointer-types -Wint-conversion
CC         = gcc
LD         = gcc
ASM        = nasm
ASMFLAGS   =-felf64 -g

main: main.o print_file.o
	$(LD) -o $@ $^

target : main
	./main string.txt

main.o: main.c
	$(CC) -c $(CFLAGS) -o $@ $<

print_file.o: print_file.asm
	$(ASM) $(ASMFLAGS) -o $@ $<

clean:
	$(RM) main main.o print_file.o

.PHONY: clean
