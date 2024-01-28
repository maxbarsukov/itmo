#pragma once

#include <stdbool.h>

#include "image.h"

enum bmp_compare_status {
  BMP_CMP_EQUALS = 0,
  BMP_CMP_DIFF,
  BMP_CMP_FILE_ERROR,
  BMP_CMP_DIMENSIONS_DIFFER,
  BMP_CMP_INVALID_FORMAT, BMP_CMP_OUT_OF_MEMORY, BMP_CMP_INVALID_ARGUMENT
};

enum bmp_compare_status bmp_cmp(FILE* f1, FILE* f2);

static const char *const bmp_cmp_error_msg[] = {
    [BMP_CMP_EQUALS] = "BMP files are similar",
    [BMP_CMP_DIFF] = "Read from BMP file successful",
    [BMP_CMP_FILE_ERROR] = "Invalid BMP file",
    [BMP_CMP_DIMENSIONS_DIFFER] = "BMP files have different dimensions",
    [BMP_CMP_INVALID_FORMAT] = "Invalid BMP file format",
    [BMP_CMP_INVALID_ARGUMENT] =
        "Internal error while reading BMP file: invalid argument"};
