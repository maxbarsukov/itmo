#include <stdlib.h>

#include "vector.h"

int main() {
    struct vector vector1 = vector_create(5);
    struct vector* vec = &vector1;

    vector_print_capacity(vec);
    vector_print_count(vec);

    for (size_t i = 1; i < 16; i++) {
        vector_setter(vec, i, i * i);
    }

    vector_print_all(vec);
    vector_print_capacity(vec);
    vector_print_count(vec);

    vector_push_back(vec, -5);

    vector_print_all(vec);
    vector_print_capacity(vec);
    vector_print_count(vec);

    struct vector vector2 = vector_create(100);
    struct vector* vec2 = &vector2;

    for (size_t i = 0; i < 10; i++) {
        vector_setter(vec2, i, 1000 - 7 * i);
    }

    vector_print_all(vec2);
    vector_print_capacity(vec2);
    vector_print_count(vec2);

    vector_add_vector_to_end(vec, vec2);

    vector_print_all(vec);
    vector_print_capacity(vec);
    vector_print_count(vec);

    vector_print_all(vec2);
    vector_print_capacity(vec2);
    vector_print_count(vec2);

    vector_set_capacity(vec2, 14);
    vector_print_all(vec2);
    vector_print_capacity(vec2);
    vector_print_count(vec2);

    vector_set_capacity(vec2, 3);
    vector_print_all(vec2);
    vector_print_capacity(vec2);
    vector_print_count(vec2);

    vector_print_to_file_data(vec, fopen("file.txt", "w"));

    vector_free(vec2);
    vector_free(vec);
    return 0;
}
