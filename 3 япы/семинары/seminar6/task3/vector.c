#include "vector.h"

#include <malloc.h>

#define MIN(x, y) (((x) < (y)) ? (x) : (y))

size_t vector_get_count(struct vector* vec) {
    return vec->count;
}

size_t vector_get_capacity(struct vector* vec) {
    return vec->capacity;
}

void vector_set_count(struct vector* vec, size_t new_count) {
    vec->count = new_count;
}

void vector_set_capacity(struct vector* vec, size_t new_capacity) {
    vec->data = realloc(vec->data, sizeof(int64_t) * new_capacity);
    vec->capacity = new_capacity;

    vector_set_count(vec, MIN(new_capacity, vector_get_count(vec)));
}

void vector_update_capacity(struct vector* vec) {
    vector_set_capacity(vec, vector_get_capacity(vec) * 2);
}

struct vector vector_create(size_t size) {
    return (struct vector) {
        .data = malloc(sizeof(int64_t) * size),
        .count = 0,
        .capacity = size
    };
}

struct maybe_int64_t vector_getter(struct vector* vec, size_t index) {
    if (index >= vector_get_capacity(vec)) {
        return (struct maybe_int64_t) { .valid = false };
    }

    return (struct maybe_int64_t) {
        .valid = true,
        .value = vec->data[index]
    };
}

bool vector_setter(struct vector* vec, size_t index, int64_t value) {
    if (vector_get_count(vec) >= vector_get_capacity(vec) - 1) {
        vector_update_capacity(vec);
    }

    if (index >= vector_get_capacity(vec)) return false;

    if (index > vector_get_count(vec)) vector_set_count(vec, index + 1);
    vec->data[index] = value;
    return true;
}

bool vector_push_back(struct vector* vec, int64_t value) {
    bool result = vector_setter(vec, vector_get_count(vec), value);
    if (result) vector_set_count(vec,vector_get_count(vec) + 1);
    return result;
}

struct vector* vector_add_vector_to_end(struct vector* source_vector, struct vector* const vector_to_add) {
    for (size_t i = 0; i < vector_get_count(vector_to_add); i++) {
        vector_push_back(source_vector, vector_getter(vector_to_add, i).value);
    }

    return source_vector;
}

void vector_print_all(struct vector* vec) {
    for (size_t i = 0; i < vector_get_count(vec); i++) {
        printf("%" PRId64 " ", vector_getter(vec, i).value);
    }
    printf("\n");
}

void vector_print_to_file_value(int64_t value, FILE* file) {
    if (file == NULL) return;
    fprintf(file, "%" PRId64 " ", value);
}

void vector_print_to_file_data(struct vector* vec, FILE* file) {
    if (file == NULL) return;
    for (size_t i = 0; i < vector_get_count(vec); i++) {
        vector_print_to_file_value(vector_getter(vec,i).value, file);
    }
}

void vector_print_capacity(struct vector* vec) {
    printf("Capacity = %zu \n", vector_get_capacity(vec));
}

void vector_print_count(struct vector* vec) {
    printf("Count = %zu \n", vector_get_count(vec));
}

void vector_free(struct vector* vec) {
    free(vec->data);
}
