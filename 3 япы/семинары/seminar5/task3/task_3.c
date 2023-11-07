#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include <inttypes.h>

void error(const char *s) {
    fprintf(stderr, "%s", s);
    abort();
}

#define print(x)                                                        \
  _Generic((x),                                                         \
           int64_t : int64_t_print(x),                                  \
           double : double_print(x),                                    \
           default : error("Unsupported operation"))

void int64_t_print(int64_t i) { printf("%" PRId64 " ", i); }
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
  struct list_##type * list_##type##_create(type value) {               \
      struct list_##type *const node = malloc(sizeof(struct list_##type));\
      node->value = value;                                              \
      node->next = 0;                                                   \
      return node;                                                      \
  }

DEFINE_LIST(int64_t)
DEFINE_LIST(double)


#define list_create(x)                        \
    _Generic((x),                             \
        int64_t: list_int64_t_create(x),      \
        double: list_double_create(x),        \
        default: error("Invalid type create") \
    )

#define list_push(x,y)                                 \
    _Generic((x),                                      \
        struct list_int64_t*: list_int64_t_push(x, y), \
        struct list_double*: list_double_push(x, y),   \
        default: error("Invalid type push")            \
    )

#define list_print(x)                                 \
    _Generic((x),                                     \
        struct list_int64_t* : list_int64_t_print(x), \
        struct list_double* : list_double_print(x),   \
        default: error("Invalid type print")          \
    )


int main() {
    struct list_int64_t *int_head = list_create((int64_t) 1);
    list_push(int_head, 2);
    list_push(int_head, 3);
    list_push(int_head, 4);
    list_push(int_head, 5);
    list_print(int_head);
    newline_print();

    struct list_double *double_head = list_create((double) 1.1);
    list_push(double_head, 2.2);
    list_push(double_head, 3.3);
    list_push(double_head, 4.4);
    list_print(double_head);
    newline_print();

    struct list_int64_t *int_head_2 = list_create((int64_t) 19);
    list_push(int_head_2, 1);
    list_print(int_head_2);
    newline_print();

    return 0;
}
