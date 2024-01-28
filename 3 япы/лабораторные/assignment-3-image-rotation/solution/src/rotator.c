#include "rotator.h"
#include "image.h"

struct image rotate(struct image* source) {
    // height <--> width
    struct image new_img = create_image(source->height, source->width);

    for (uint64_t y = 0; y < source->height; ++y) {
        for(uint64_t x = 0; x < source->width; ++x) {
            new_img.data[x * new_img.width + new_img.width - y - 1] = source->data[y * source->width + x];
        }
    }

    destroy_image(source);
    return new_img;
}

struct image rotate_angle(struct image* source, int32_t angle) {
    uint16_t times = angle / 90;
    if (angle == 0) times = 4;

    struct image rotated = *source;
    for (uint16_t i = 0; i < times; ++i) {
        rotated = rotate(&rotated);
    }
    return rotated;
}
