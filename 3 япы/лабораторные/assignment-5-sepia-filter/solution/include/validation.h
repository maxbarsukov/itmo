#ifndef VALIDATION_HEADER
#define VALIDATION_HEADER

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

enum status {
    OK = 0,
    ERR_OPEN,
    ERR_CLOSE,
    ERR_CANT_SAVE,
    ERR_ARGS_COUNT,
    ERR_SIGNATURE,
    ERR_HEADER,
    ERR_INVALID_BITS,
    ERR_INVALID_BIT_COUNT
};

struct input_data {
    enum status status_code;
    FILE* in;
    FILE* out;
};

struct input_data parse_input(char** argv);
enum status print_error(enum status s);
enum status close_file(FILE* file);

#endif
