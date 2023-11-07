#include <stdio.h>

#define print_var(x) printf(#x " is %d", x )

int main() {
   int x = 156;
   const int Y = 6532;

   print_var(42);
   print_var(x);
   print_var(__INT32_MAX__);
   print_var(Y);

   return 0;
}
