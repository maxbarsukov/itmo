#ifndef ROTATOR_HEADER
#define ROTATOR_HEADER

#include "image.h"

struct image rotate(struct image* source);
struct image rotate_angle(struct image* source, int32_t angle);

#endif
