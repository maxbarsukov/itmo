#define TEST_SMART_MMAP

#include "test.h"


static const size_t corner_cases[] = { 0, 10, 24, 24 + offsetof(struct block_header, contents),
    REGION_MIN_SIZE - offsetof(struct block_header, contents), REGION_MIN_SIZE };

static size_t test_length = 0;
static int mmap_counter = 0;

DEFINE_MMAP_IMPL(fail) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);
    assert(addr == HEAP_START && length == region_actual_size(test_length + offsetof(struct block_header, contents)));
    ++mmap_counter;
    return MAP_FAILED;
}

// test when mmap failed
DEFINE_TEST(fail) {
    current_mmap_impl = MMAP_IMPL(fail);

    for (size_t i = 0; i < sizeof(corner_cases) / sizeof(*corner_cases); ++i) {
        test_length = corner_cases[i];
        mmap_counter = 0;

        fprintf(stderr, "heap_init(%zu)\n", test_length);

        assert(heap_init(test_length) == NULL);
        assert(mmap_counter == 2);
    }
}

static void * mmap_result = NULL;

DEFINE_MMAP_IMPL(success) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);
    assert(addr == HEAP_START && length == region_actual_size(test_length + offsetof(struct block_header, contents)));
    ++mmap_counter;
    return (mmap_result = mmap(addr, length, prot, flags, fd, offset));
}

// test when mmap success
DEFINE_TEST(success) {
    current_mmap_impl = MMAP_IMPL(success);

    for (size_t i = 0; i < sizeof(corner_cases) / sizeof(*corner_cases); ++i) {
        test_length = corner_cases[i];
        mmap_counter = 0;

        fprintf(stderr, "heap_init(%zu)\n", test_length);

        void * const result = heap_init(test_length);

        assert(result == mmap_result);
        assert(mmap_counter == 1);

        munmap(result, region_actual_size(test_length + offsetof(struct block_header, contents)));
    }
}

int main() {
    RUN_SINGLE_TEST(fail);
    RUN_SINGLE_TEST(success);
    return 0;
}
