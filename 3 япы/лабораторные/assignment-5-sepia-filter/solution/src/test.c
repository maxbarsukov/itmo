#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "image.h"
#include "io.h"
#include "sepia_filter.h"
#include "sepia_simd_filter.h"
#include "validation.h"

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

void my_memcpy(void *dest, const void *src, size_t n) {
  for (size_t i = 0; i < n; i++) {
    ((char*)dest)[i] = ((char*)src)[i];
  }
}

struct image clone_image(const struct image *img) {
  struct image copy =  create_image(img->width, img->height);
  my_memcpy(copy.data, img->data, img->width * img->height * sizeof(struct pixel));
  return copy;
}

double benchmark_sepia(char* filepath, uint16_t runs, bool use_simd) {
    struct image img;
    enum read_status rs = from_bmp(fopen(filepath, "rb"), &img);
    if (rs != READ_OK) {
      printf("No such file.");
      exit(rs);
    }

    double elapsed_time = 0;
    clock_t start_time, end_time;
    for (uint16_t run = 0; run < runs; run++) {
      struct image cloned_image = clone_image(&img);

      if (use_simd) {
        start_time = clock();
        make_sepia_with_simd(&cloned_image);
        end_time = clock();
      } else {
        start_time = clock();
        make_sepia(&cloned_image);
        end_time = clock();
      }

      elapsed_time += end_time - start_time;
      free(cloned_image.data);
    }

    destroy_image(&img);
    return elapsed_time / CLOCKS_PER_SEC;
}

int main(void) {
  printf("Little file (192.1 KiB), 1000 times, without SIMD: -------------------- c --- %f seconds\n", benchmark_sepia("files/snail.bmp", 1000, false));
  printf("Little file (192.1 KiB), 1000 times, with SIMD: --------------------- asm --- %f seconds\n\n", benchmark_sepia("files/snail.bmp", 1000, true));
  printf("Little file (192.1 KiB), 10_000 times, without SIMD: ------------------ c --- %f seconds\n", benchmark_sepia("files/snail.bmp", 10000, false));
  printf("Little file (192.1 KiB), 10_000 times, with SIMD: ------------------- asm --- %f seconds\n\n", benchmark_sepia("files/snail.bmp", 10000, true));
  printf("Big file (5.9 MiB), 100 times, without SIMD: -------------------------- c --- %f seconds\n", benchmark_sepia("files/big.bmp", 100, false));
  printf("Big file (5.9 MiB), 100 times, with SIMD: --------------------------- asm --- %f seconds\n\n", benchmark_sepia("files/big.bmp", 100, true));
  return 0;
}
