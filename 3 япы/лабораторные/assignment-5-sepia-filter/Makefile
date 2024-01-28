NAME := sepia-filter

## BMP samples

FILES_DIR = files
FILES = $(FILES_DIR)/snail
FILE_OUTPUT_C = $(FILES:=_out_c.bmp)
FILE_OUTPUT_ASM = $(FILES:=_out_asm.bmp)

## Compiling

CC = clang
LINKER = $(CC)
CFLAGS = -O3 -Wall -Wextra -fsanitize=address -pedantic -c -g -fPIC
DEPFLAGS = -MMD -MP
INCLUDEFLAGS = -Isolution/include
LDFLAGS = -fuse-ld=lld -lm -fsanitize=address
NASM = nasm
ASMFLAGS = -f elf64 -g

SRC_DIR = solution/src
ASM_DIR = solution/asm
OBJ_DIR = obj
BIN_DIR = out

SRC	= $(wildcard $(SRC_DIR)/*.c)
ASM	= $(wildcard $(ASM_DIR)/*.asm)
OBJ_C	= $(SRC:$(SRC_DIR)/%.c=$(OBJ_DIR)/%.o)
OBJ_ASM	= $(ASM:$(ASM_DIR)/%.asm=$(OBJ_DIR)/%.asm.o)

BIN_C = $(BIN_DIR)/solution_c
BIN_ASM = $(BIN_DIR)/solution_asm
BIN_TEST = $(BIN_DIR)/test

FORMAT_STYLE = LLVM

all: $(BIN_C) $(BIN_ASM) $(BIN_TEST) $(FILE_OUTPUT_C) $(FILE_OUTPUT_ASM)

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.c | $(OBJ_DIR)
	$(CC) $(DEPFLAGS) $(CFLAGS) $(INCLUDEFLAGS) -o $@ $<

$(OBJ_DIR)/%.asm.o: $(ASM_DIR)/%.asm | $(OBJ_DIR)
	$(NASM) -MP -MD $@.d $(ASMFLAGS) $< -o $@

$(OBJ_DIR)/solution_c.o: $(SRC_DIR)/main.c | $(OBJ_DIR)
	$(CC) $(DEPFLAGS) $(CFLAGS) $(INCLUDEFLAGS) -o $@ $<

$(OBJ_DIR)/solution_asm.o: $(SRC_DIR)/main.c | $(OBJ_DIR)
	$(CC) -DSIMD $(DEPFLAGS) $(CFLAGS) $(INCLUDEFLAGS) -o $@ $<

$(BIN_C): $(filter-out $(OBJ_DIR)/main.o $(OBJ_DIR)/test.o, $(OBJ_C)) $(OBJ_ASM) $(OBJ_DIR)/solution_c.o | $(BIN_DIR)
	$(CC) $(LDFLAGS) -o $@ $^

$(BIN_ASM): $(filter-out $(OBJ_DIR)/main.o $(OBJ_DIR)/test.o, $(OBJ_C)) $(OBJ_ASM) $(OBJ_DIR)/solution_asm.o | $(BIN_DIR)
	$(CC) -DSIMD $(DEPFLAGS) $(LDFLAGS) -o $@ $^

$(BIN_TEST): $(filter-out $(OBJ_DIR)/main.o, $(OBJ_C)) $(OBJ_ASM) | $(BIN_DIR)
	$(CC) -DSIMD $(DEPFLAGS) $(LDFLAGS) -o $@ $^

$(BIN_DIR) $(OBJ_DIR):
	mkdir -p $@

.PHONY: clean format test

test: $(BIN_TEST)
	$(BIN_TEST)

clean:
	rm -f files/*out*.bmp
	rm -rf $(OBJ_DIR) $(BIN_DIR)

files/%_out_asm.bmp: files/%.bmp $(BIN_ASM)
	$(BIN_ASM) $< $@

files/%_out_c.bmp: files/%.bmp $(BIN_C)
	$(BIN_C) $< $@

format:
	clang-format -style=$(FORMAT_STYLE) -i src/*.c include/*.h

tidy:
	clang-tidy src/*.c include/*.h

-include $(OBJ_ASM:.o=.d)
-include $(OBJ_C:.o=.d)
