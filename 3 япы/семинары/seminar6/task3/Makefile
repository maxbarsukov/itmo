CFLAGS 	= -g -O2 -Wall -Werror -std=c17 -Wno-unused-function -Wdiscarded-qualifiers -Wincompatible-pointer-types -Wint-conversion -fno-plt
CC     	= gcc
LD     	= gcc

SOURCES = main.c vector.h vector.c
OBJECTS 	= $(SOURCES:.c=.o)
EXECUTABLE 	= main.out

all: $(SOURCES) $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS)
	$(LD) -o $@ $^

%.o: %.c
	$(CC) -c $(CFLAGS) -o $@ $<

clean:
	$(RM) $(EXECUTABLE) *.o

run:
	./$(EXECUTABLE)

.PHONY: clean all run
