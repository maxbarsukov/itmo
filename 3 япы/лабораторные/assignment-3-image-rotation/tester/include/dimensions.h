#pragma once

#include <stddef.h>

struct dimensions {
  size_t x;
  size_t y;
};

struct dimensions dimensions_reverse( const struct dimensions* dim );
