#include "io.h"

void putchar(char ch);

void
puts(const char *s)
{
  while (*s)
  {
      putchar(*s++);
  }
}
