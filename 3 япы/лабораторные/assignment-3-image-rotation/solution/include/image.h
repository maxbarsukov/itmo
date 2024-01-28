#ifndef IMAGE_HEADER
#define IMAGE_HEADER

#include <stdint.h>

struct __attribute__((packed)) pixel {
    uint8_t b, g, r;
};

struct image {
    uint64_t width, height;
    struct pixel* data;
};

struct image create_image(uint64_t width, uint64_t height);
void destroy_image(struct image* img);

#endif
