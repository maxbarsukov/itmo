#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>

#include "util.h"

_Noreturn void err( const char* msg, ... ) {
  va_list args;
  va_start (args, msg);
  vfprintf(stderr, msg, args); // NOLINT 
  va_end (args);
  abort();
}


extern inline size_t size_max( size_t x, size_t y );
