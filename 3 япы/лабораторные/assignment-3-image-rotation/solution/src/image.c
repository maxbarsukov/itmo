#include <stdlib.h>

#include "image.h"

struct image create_image(uint64_t width, uint64_t height) {
    return (struct image) {
        .width = width,
        .height = height,
        .data = malloc(sizeof(struct pixel) * width * height)
    };
}

void destroy_image(struct image* img) {
    free(img->data);
}
