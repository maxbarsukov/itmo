ASM=nasm
ASMFLAGS=-f elf64
LD=ld
PYTHON=python3

ASM_FILES=$(wildcard $(src)/*.asm)
INC_FILES=$(wildcard $(src)/*.inc)
OBJ_FILES=$(ASM_FILES:$(src)/%.asm=$(bin)/%.o)

bin=bin
src=src
spec=spec

$(bin)/%.o: $(src)/%.asm
	$(ASM) $(ASMFLAGS) -o $@ $<

bin/main.o: $(src)/main.asm $(INC_FILES)

app: $(OBJ_FILES)
	$(LD) -o $(bin)/$@ $^


.PHONY: run clean test

run: app
	./bin/app

clean:
	rm -f $(bin)/*.o
	rm -f $(bin)/app

test: app
	$(PYTHON) $(spec)/test.py
