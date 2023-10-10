ASM=nasm
ASMFLAGS=-f elf64
LD=ld

lib.o: lib.asm
	$(ASM) $(ASMFLAGS) -o $@ $<

main.o: main.asm lib.o
	$(ASM) $(ASMFLAGS) -o $@ $<

app: lib.o main.o
	$(LD) -o $@ $< main.o

.PHONY: clean
clean:
	rm *.o
