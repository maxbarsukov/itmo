#include "sepia_filter.h"

static uint8_t saturation(uint64_t x) {
  return x < 256 ? x : 255;
}

static const float c[3][3] = {
    {.393f, .769f, .189f},
    {.349f, .686f, .168f},
    {.272f, .534f, .131f}
};

// From https://stackoverflow.com/a/9449159
void make_pixel_sepia(struct pixel *pixel) {
  const struct pixel inp = *pixel;
  pixel->r = saturation(inp.r * c[0][0] + inp.g * c[0][1] + inp.b * c[0][2]);
  pixel->g = saturation(inp.r * c[1][0] + inp.g * c[1][1] + inp.b * c[1][2]);
  pixel->b = saturation(inp.r * c[2][0] + inp.g * c[2][1] + inp.b * c[2][2]);
}

//! Changes source image
void make_sepia(struct image* img) {
  uint64_t pixels_count = img->height * img->width;

  for (uint64_t i = 0; i < pixels_count; i++) {
    make_pixel_sepia(img->data + i);
  }
}
