#ifndef VECTOR_H
#define VECTOR_H

#include <inttypes.h>
#include <stdio.h>
#include <stdbool.h>

struct vector {
    int64_t *data;
    size_t capacity;
    size_t count;
};

struct maybe_int64_t {
    int64_t value;
    bool valid;
};

size_t vector_get_count(struct vector* vec);
size_t vector_get_capacity(struct vector* vec);

void vector_set_count(struct vector* vec, size_t new_count);
void vector_set_capacity(struct vector* vec, size_t new_capacity);
void vector_update_capacity(struct vector* vec);

struct vector vector_create(size_t size);
void vector_free(struct vector* vec);

struct maybe_int64_t vector_getter(struct vector* vec, size_t index);
bool vector_setter(struct vector* vec, size_t index, int64_t value);

bool vector_push_back(struct vector* vec, int64_t value);
struct vector* vector_add_vector_to_end(struct vector* source_vector, struct vector* vector_to_add);

void vector_print_all(struct vector* vec);
void vector_print_to_file_value(int64_t value, FILE* file);
void vector_print_to_file_data(struct vector* vec, FILE* file);
void vector_print_capacity(struct vector* vec);
void vector_print_count(struct vector* vec);

#endif
