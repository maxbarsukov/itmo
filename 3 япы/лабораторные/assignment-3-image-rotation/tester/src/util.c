#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>

_Noreturn void err( const char* msg, ... ) {
  va_list args;
  va_start (args, msg);
  // There is a bug in clang-tidy that makes it consider args as uninitialized
  // NOLINT helps supress this message
  // See: https://bugs.llvm.org/show_bug.cgi?id=41311
  vfprintf(stderr, msg, args); // NOLINT
  va_end (args);
  exit(1);
}

