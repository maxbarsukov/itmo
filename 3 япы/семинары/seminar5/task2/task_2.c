#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include <inttypes.h>

#define _print(type, x) type##_print(x)

void int64_t_print(int64_t i) { printf("%" PRId64, i); }
void double_print(double d) { printf("%lf ", d); }
void newline_print() { puts(""); }

#define DEFINE_LIST(type)                                               \
  struct list_##type {                                                  \
    type value;                                                         \
    struct list_##type* next;                                           \
  };                                                                    \
  void list_##type##_push(struct list_##type *list, type value) {       \
    struct list_##type *const node = malloc(sizeof(struct list_##type));\
    while (list->next != 0)                                             \
      list = list->next;                                                \
    node->value = value;                                                \
    node->next = 0;                                                     \
    list->next = node;                                                  \
  }                                                                     \
  void list_##type##_print(struct list_##type *list) {                  \
    while (list != 0) {                                                 \
      type##_print(list->value);                                        \
      list = list->next;                                                \
    }                                                                   \
  }                                                                     \
  struct list_##type * list_##type##_create(type value) {                  \
      struct list_##type *const node = malloc(sizeof(struct list_##type));\
      node->value = value;                                              \
      node->next = 0;                                                   \
      return node;                                                      \
  }

#define push(type, head, value) list_##type##_push(head, value);
#define print_list(type, head) list_##type##_print(head);

DEFINE_LIST(int64_t)
DEFINE_LIST(double)

int main() {
    struct list_int64_t *int_head = list_int64_t_create(1);
    push(int64_t, int_head, 2);
    push(int64_t, int_head, 3);
    push(int64_t, int_head, 4);
    print_list(int64_t, int_head);
    newline_print();

    struct list_double *double_head = list_double_create(9.1);
    push(double , double_head, 8.1);
    push(double , double_head, 7.1);
    print_list(double , double_head);
    newline_print();

    struct list_int64_t *int_head_2 = list_int64_t_create(19);
    push(int64_t, int_head_2, 1);
    print_list(int64_t, int_head_2);
    newline_print();

    return 0;
}
