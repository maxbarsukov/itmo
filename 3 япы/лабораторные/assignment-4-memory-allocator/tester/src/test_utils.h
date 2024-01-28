#pragma once

#define _GNU_SOURCE

#include <stdio.h>
#include <sys/mman.h>
#include <assert.h>


#define DEFINE_TEST(_name) static void test_##_name()

#define DEFINE_TEST_GROUP(_name) static const test_in_group tests_##_name[] =

#define RUN_SINGLE_TEST(_name) do { \
    puts(" Run test \"" #_name "\"..."); \
    test_##_name(); \
} while (0)

#define RUN_TEST_GROUP(_name) run_test_group( \
    #_name, \
    tests_##_name, \
    sizeof(tests_##_name) / sizeof(*tests_##_name) \
)

#define TEST(_name) test_##_name

#define TEST_IN_GROUP(_name) { .name = #_name, .test = test_##_name, }


typedef void (*test_function)();
typedef struct {
    const char * name;
    test_function test;
} test_in_group;

inline void run_test_group(const char * name, const test_in_group * tests, size_t amount) {
    printf(" Run test group \"%s\":\n", name);

    for (size_t i = 0; i < amount; ++i) {
        printf("  %zu. Run test \"%s\"...\n", i + 1, tests[i].name);
        tests[i].test();
    }
}

inline void base_mmap_checks(void * addr, size_t length, int prot, int flags, int fd, off_t offset) {
    (void) addr;

    assert(length > 0); // from man
    assert(prot == (PROT_READ | PROT_WRITE));
    assert(flags & (MAP_PRIVATE | MAP_ANONYMOUS));
    assert((flags | MAP_FIXED)
        || (flags | MAP_FIXED_NOREPLACE)
        || (flags & (~(MAP_PRIVATE | MAP_ANONYMOUS))) == 0);
    assert(fd == -1); // from man
    assert(offset == 0); // from man
}

void print_mmap_call(FILE * output, void * addr, size_t length, int prot, int flags, int fd, off_t offset);
void print_mmap_result(FILE * output, void * retval);


#ifdef TEST_SMART_MMAP
#define DEFINE_MMAP_IMPL(_name) \
static void * mmap_impl_##_name(void * addr, size_t length, int prot, int flags, int fd, off_t offset)

#define MMAP_IMPL(_name) mmap_impl_##_name

typedef void * (*mmap_impl_t)(void *, size_t, int, int, int, off_t);

static mmap_impl_t current_mmap_impl = NULL;

static inline void * _mmap(void * addr, size_t length, int prot, int flags, int fd, off_t offset) {
    void * result = NULL;

    print_mmap_call(stderr, addr, length, prot, flags, fd, offset);

    if (current_mmap_impl) {
        result = current_mmap_impl(addr, length, prot, flags, fd, offset);
    } else {
        result = MAP_FAILED;
    }

    print_mmap_result(stderr, result);
    return result;
}
#endif
