#include <inttypes.h>
#include <stdbool.h>
#include <stdio.h>

#include "bmp.h"
#include "common.h"
#include "file_cmp.h"
#include "image.h"
#include "io.h"

#define FOR_HEADER(FOR_FIELD)                                                  \
  FOR_FIELD(uint8_t, bfType[2])                                                \
  FOR_FIELD(uint32_t, bfileSize)                                               \
  FOR_FIELD(uint32_t, bfReserved)                                              \
  FOR_FIELD(uint32_t, bOffBits)                                                \
  FOR_FIELD(uint32_t, biSize)                                                  \
  FOR_FIELD(uint32_t, biWidth)                                                 \
  FOR_FIELD(uint32_t, biHeight)                                                \
  FOR_FIELD(uint16_t, biPlanes)                                                \
  FOR_FIELD(uint16_t, biBitCount)                                              \
  FOR_FIELD(uint32_t, biCompression)                                           \
  FOR_FIELD(uint32_t, biSizeImage)                                             \
  FOR_FIELD(uint32_t, biXPelsPerMeter)                                         \
  FOR_FIELD(uint32_t, biYPelsPerMeter)                                         \
  FOR_FIELD(uint32_t, biClrUsed)                                               \
  FOR_FIELD(uint32_t, biClrImportant)

#define DECLARE_FIELD(t, n) t n;

#pragma pack(push, 1)
struct header {
  FOR_HEADER(DECLARE_FIELD)
};
#pragma pack(pop)

#undef FOR_HEADER
#undef DECLARE_FIELD

static size_t get_padding( size_t width ) {return width % 4;}
static bool header_is_correct(const struct header *header) {

  if (header->bfType[0] != 'B' || header->bfType[1] != 'M')
    return false;
  if (header->biBitCount != 24)
    return false;

  return true;
}

enum bmp_compare_status bmp_cmp(FILE *f1, FILE *f2) {
  struct header h1 = {0}, h2 = {0};

  if (!fread(&h1, sizeof(struct header), 1, f1) ||
      !header_is_correct(&h1))
    return BMP_CMP_FILE_ERROR;

  if (!fread(&h2, sizeof(struct header), 1, f2) ||
      !header_is_correct(&h2))
    return BMP_CMP_INVALID_FORMAT;

  if (h1.biWidth != h2.biWidth || h1.biHeight != h2.biHeight)
    return BMP_CMP_DIMENSIONS_DIFFER;

  const size_t padding = get_padding(h1.biWidth);

  for( size_t i = 0; i < h1.biHeight; i++ ) {
    switch (file_cmp( f1, f2, h1.biWidth * 3 ) ) {
    case CMP_DIFF: return BMP_CMP_DIFF;
    case CMP_ERROR: return BMP_CMP_FILE_ERROR;
    case CMP_EQ:
      if (fseek(f1, (long) padding, SEEK_CUR) != 0 ||
          fseek(f2, (long) padding, SEEK_CUR) != 0)
        return BMP_CMP_FILE_ERROR;
      break;
    default: err("Implementation error"); break;
    }
  }
  return BMP_CMP_EQUALS;
}
