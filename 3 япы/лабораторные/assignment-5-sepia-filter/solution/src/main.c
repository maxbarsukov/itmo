#include <stdio.h>

#include "image.h"
#include "io.h"
#include "sepia_filter.h"
#include "validation.h"

#ifdef SIMD
  #include "sepia_simd_filter.h"
#endif


#define ARGS_COUNT 3

enum status read_bmp(FILE* in, struct image* img) {
    enum read_status rs = from_bmp(in, img);
    if (rs != READ_OK) {
        enum status status_to_message[] = {
                OK,ERR_SIGNATURE,ERR_INVALID_BITS,ERR_HEADER,ERR_INVALID_BIT_COUNT
        };
        return print_error(status_to_message[rs]);
    }
    return close_file(in);
}

enum status write_bmp(FILE* out, const struct image* img) {
    enum write_status ws = to_bmp(out, img);
    if (ws != WRITE_OK) {
        close_file(out);
        return print_error(ERR_CANT_SAVE);
    }
    return close_file(out);
}

int main(int argc, char** argv) {
    if (argc != ARGS_COUNT) return print_error(ERR_ARGS_COUNT);

    // parse input
    struct input_data input = parse_input(argv);
    if (input.status_code != 0) {
        return print_error(input.status_code);
    }

    // read bmp
    struct image img;
    enum status rs = read_bmp(input.in, &img);
    if (rs != OK) return rs;

    printf("Image loaded\n");

    #ifdef SIMD
      make_sepia_with_simd(&img);
      printf("Sepia filtering (SIMD) done!\n");
    #else
      make_sepia(&img);
      printf("Sepia filtering done!\n");
    #endif

    // write
    enum status ws = write_bmp(input.out, &img);
    if (ws == OK) printf("Image saved\n");

    destroy_image(&img);
    return ws;
}
