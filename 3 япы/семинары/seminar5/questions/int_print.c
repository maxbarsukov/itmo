/* int_print.c */

#include <inttypes.h>
#include <stdio.h>
#include <stdlib.h>

#define _print(type, x) type##_print(x)

void int64_t_print(int64_t i) { printf("%" PRId64, i); }
void double_print(double d) { printf("%lf", d); }
void newline_print() { puts(""); }

int main() {
  int64_t x = 42;
  double  d = 99.99;

  _print(int64_t, x);
  _print(newline, "");
  _print(double, d);
  _print(newline, "");

  return 0;
}
