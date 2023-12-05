/* tail-rec.c */

#include <inttypes.h>
#include <malloc.h>
#include <stddef.h>
#include <stdio.h>

void print_size(size_t i) { printf("%zu" , i); }

struct list {
  int64_t elem;
  struct list *next;
};

struct list *c(int64_t head, struct list *tail) {
  struct list *h = (struct list *)malloc(sizeof(struct list));
  h->elem = head;
  h->next = tail;
  return h;
}

size_t list_length(struct list const *l) {
  if (!l)
    return 0;
  return 1 + list_length(l->next);
}

int main(int argc, char **argv) {
  const size_t len = 1024 * 1024;

  struct list *lst = NULL;

  for( size_t i = 0; i < len; i++) {
    lst = c(i, lst);
  }

  print_size(list_length(lst));
  return 0;
}
