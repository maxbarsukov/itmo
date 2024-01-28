#ifndef IO_HEADER
#define IO_HEADER

#include <stdio.h>

#include "image.h"

enum read_status {
    READ_OK = 0,
    READ_INVALID_SIGNATURE,
    READ_INVALID_BITS,
    READ_INVALID_HEADER,
    READ_INVALID_BIT_COUNT
};

enum write_status {
    WRITE_OK = 0,
    WRITE_ERROR
};

enum read_status from_bmp(FILE* in, struct image* img);
enum write_status to_bmp(FILE* out, struct image const* img);

#endif
