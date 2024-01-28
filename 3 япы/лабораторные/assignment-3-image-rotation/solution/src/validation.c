#include "validation.h"

const char *error_messages[] = {
        "OK", // OK
        "Failed to open file\n", // ERR_OPEN
        "Failed to close file\n", // ERR_CLOSE
        "Failed to save image\n", // ERR_CANT_SAVE
        "Not enough arguments\n", // ERR_ARGS_COUNT
        "Failed to rotate: Cannot parse angle\n", // ERR_CANT_PARSE_ANGLE
        "Failed to rotate: Angle should be in [0, 90, -90, 180, -180, 270, -270]\n", // ERR_BAD_ANGLE
        "Failed to load image: Invalid signature\n", // ERR_SIGNATURE
        "Failed to load image: Invalid header\n", // ERR_HEADER
        "Failed to load image: Invalid bits\n", // ERR_INVALID_BITS
        "Failed to load image: Bit count is not supported\n", // ERR_INVALID_BIT_COUNT
};

enum status print_error(enum status s) {
    printf("%s", error_messages[s]);
    return s;
}

enum status close_file(FILE* file) {
    if (fclose(file) != 0) {
        return print_error(ERR_CLOSE);
    }
    return OK;
}

struct input_data parse_input(char** argv) {
    FILE* in = fopen(argv[1], "rb");
    FILE* out = fopen(argv[2], "wb");

    if (in == NULL || out == NULL) {
        return (struct input_data) { .status_code =  ERR_OPEN };
    }

    char *p;
    long converted = strtol(argv[3], &p, 10);
    if (*p != '\0' || converted > INT_MAX || converted < INT_MIN) {
        return (struct input_data) { .status_code =  ERR_CANT_PARSE_ANGLE };
    }

    int32_t angle = -((int32_t) converted);
    if (angle < 0) angle = 360 + angle;
    if (angle != 0 && angle != 90 && angle != 180 && angle != 270) {
        return (struct input_data) { .status_code =  ERR_BAD_ANGLE };
    }

    return (struct input_data) {
            .status_code = OK,
            .in = in,
            .out = out,
            .angle = angle % 360,
    };
}
