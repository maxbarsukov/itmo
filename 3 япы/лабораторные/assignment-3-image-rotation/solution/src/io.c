#include <stdio.h>
#include <stdlib.h>

#include "bmp.h"
#include "io.h"

// 0x4D42 == MB <-- ([BM]P)
#define HEADER_TYPE 0x4D42
#define HEADER_RESERVED 0
#define HEADER_SIZE  40
#define HEADER_PLANES 1
#define HEADER_BIT_COUNT 24
#define HEADER_COMPRESSION 0
// Not so important, 72 DPI ~ 2835
#define HEADER_PIXELS_PER_METER 2835
#define HEADER_COLORS_USED 0
#define HEADER_COLORS_IMPORTANT 0

static inline uint32_t get_padding_size(const struct image* img) {
    return (4 - ((img->width * sizeof(struct pixel)) % 4)) % 4;
}

enum write_status to_bmp(FILE* out, const struct image* img) {
    uint32_t padding_size = get_padding_size(img);
    uint32_t row_with_padding = img->width * sizeof(struct pixel) + padding_size;

    struct bmp_header header = {
            .bfType = HEADER_TYPE,
            .biWidth = img->width,
            .biHeight = img->height,
            .biSizeImage = row_with_padding * img->height,
            .bOffBits = sizeof(struct bmp_header),
            .bfileSize = sizeof(struct bmp_header) + row_with_padding * img->height,
            .bfReserved = HEADER_RESERVED,
            .biSize = HEADER_SIZE,
            .biPlanes = HEADER_PLANES,
            .biBitCount = HEADER_BIT_COUNT,
            .biCompression = HEADER_COMPRESSION,
            .biXPelsPerMeter = HEADER_PIXELS_PER_METER,
            .biYPelsPerMeter = HEADER_PIXELS_PER_METER,
            .biClrUsed = HEADER_COLORS_USED,
            .biClrImportant = HEADER_COLORS_IMPORTANT
    };

    if (fwrite(&header, sizeof(struct bmp_header), 1, out) != 1) {
        return WRITE_ERROR;
    }

    for (uint32_t y = 0; y < img->height; ++y) {
        if (fwrite(&img->data[img->width * y], sizeof(struct pixel), img->width, out) != img->width) {
            // cannot write pixels
            return WRITE_ERROR;
        }

        // add padding
         fseek(out, padding_size, SEEK_CUR);
    }

    return WRITE_OK;
}

enum read_status from_bmp(FILE* in, struct image* img) {
    struct bmp_header header;

    if (fread(&header, sizeof(struct bmp_header), 1, in) != 1) return READ_INVALID_HEADER;
    if (header.bfType != HEADER_TYPE) return READ_INVALID_SIGNATURE;
    if (header.biBitCount != HEADER_BIT_COUNT) return READ_INVALID_BIT_COUNT;

    *img = create_image(header.biWidth, header.biHeight);
    uint32_t padding_size = get_padding_size(img);

    if (!(img->data)) return READ_INVALID_BITS;
    for (uint32_t y = 0; y < header.biHeight; ++y) {
        fread(&(*img).data[y * header.biWidth], sizeof(struct pixel), header.biWidth, in);
        fseek(in, (long) padding_size, SEEK_CUR); // skip padding
    }

    return READ_OK;
}
