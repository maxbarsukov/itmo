#include <stdbool.h>
#include <stdio.h>

#include "bmp.h"
#include "common.h"
#include "io.h"

void usage(void) {
  fprintf(stderr,
          "Usage: ./" EXECUTABLE_NAME " BMP_FILE_NAME1 BMP_FILE_NAME2\n");
}

int main(int argc, char **argv) {
  if (argc != 3)
    usage();

  // error handling should be here
  FILE *f1 = fopen(argv[1], "rb");
  if (!f1)
    err("Bad first input file\n");
  FILE *f2 = fopen(argv[2], "rb");
  if (!f2) {
    fclose(f1);
    err("Bad second input file\n");
  }

  const enum bmp_compare_status status = bmp_cmp(f1, f2);

  fclose(f1);
  fclose(f2);

  if (status == BMP_CMP_EQUALS)
    return 0;

  fprintf(stderr, "%s\n", bmp_cmp_error_msg[status]);
  return status;
}
