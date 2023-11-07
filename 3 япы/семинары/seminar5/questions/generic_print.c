/* generic_print.c */

#include <inttypes.h>
#include <stdio.h>
#include <stdlib.h>

void error(const char *s) {
  fprintf(stderr, "%s", s);
  abort();
}

#define _print(type, x) type##_print(x)

// Обратите внимание на обратные слэши в конце каждой строчки кроме последней!
// Они экранируют переводы строк, позволяя написать макрос во много строчек.
#define print(x)                                                        \
  _Generic((x),                                                         \
           int64_t : int64_t_print(x),                                  \
           double : double_print(x),                                    \
           default : error("Unsupported operation"))

void int64_t_print(int64_t i) { printf("%" PRId64, i); }
void double_print(double d) { printf("%lf", d); }
void newline_print() { puts(""); }

int main() {
  int64_t x = 42;
  double d = 99.99;

  print(x);
  newline_print();
  print(d);
  newline_print();
  
  return 0;
}
