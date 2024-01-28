#ifndef VALIDATION_HEADER
#define VALIDATION_HEADER

#include <limits.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

enum status {
    OK = 0,
    ERR_OPEN,
    ERR_CLOSE,
    ERR_CANT_SAVE,
    ERR_ARGS_COUNT,
    ERR_CANT_PARSE_ANGLE,
    ERR_BAD_ANGLE,
    ERR_SIGNATURE,
    ERR_HEADER,
    ERR_INVALID_BITS,
    ERR_INVALID_BIT_COUNT
};

struct input_data {
    enum status status_code;
    FILE* in;
    FILE* out;
    int32_t angle;
};

struct input_data parse_input(char** argv);
enum status print_error(enum status s);
enum status close_file(FILE* file);

#endif
