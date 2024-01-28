#define TEST_SMART_MMAP

#include "test.h"

#include <string.h>


static int test_optimistic_case_mmap_counter = 0;

DEFINE_MMAP_IMPL(optimistic_case) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);

    ++test_optimistic_case_mmap_counter;
    if (test_optimistic_case_mmap_counter == 1) {
        assert((flags & MAP_FIXED) || (flags & MAP_FIXED_NOREPLACE));
    } else if (test_optimistic_case_mmap_counter == 2) {
        assert((~flags & MAP_FIXED) && (~flags & MAP_FIXED_NOREPLACE));
    } else {
        assert(false);
    }

    return mmap(addr, length, prot, flags, fd, offset);
}

// test with real HEAP_START and successful mmap MAP_FIXED
DEFINE_TEST(optimistic_case) {
    current_mmap_impl = MMAP_IMPL(optimistic_case);

    const struct region region = alloc_region(HEAP_START, 0);

    assert(region.addr == HEAP_START);
    assert(region.size == REGION_MIN_SIZE);
    assert(region.extends);

    assert(test_optimistic_case_mmap_counter == 1);

    struct block_header * block = region.addr;
    assert(block->next == NULL);
    assert(block->capacity.bytes == REGION_MIN_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free);

    memset(block->contents, 42, block->capacity.bytes);

    munmap(region.addr, region.size);
}

static int test_map_fixed_failed_mmap_counter = 0;

DEFINE_MMAP_IMPL(map_fixed_failed) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);

    ++test_map_fixed_failed_mmap_counter;
    if (test_map_fixed_failed_mmap_counter == 1) {
        return MAP_FAILED;
    } else {
        return mmap(NULL, length, prot, flags, fd, offset);
    }

    return MAP_FAILED;
}

// test with real HEAP_START and failing mmap MAP_FIXED
DEFINE_TEST(map_fixed_failed) {
    current_mmap_impl = MMAP_IMPL(map_fixed_failed);

    const struct region region = alloc_region(HEAP_START, 0);

    assert(region.addr != HEAP_START);
    assert(region.size == REGION_MIN_SIZE);
    assert(!region.extends);

    assert(test_map_fixed_failed_mmap_counter == 2);

    struct block_header * block = region.addr;
    assert(block->next == NULL);
    assert(block->capacity.bytes == REGION_MIN_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free);

    memset(block->contents, 42, block->capacity.bytes);

    munmap(region.addr, region.size);
}

static int test_pessimistic_case_mmap_counter = 0;

DEFINE_MMAP_IMPL(pessimistic_case) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);

    ++test_pessimistic_case_mmap_counter;
    return MAP_FAILED;
}

// test with real HEAP_START and failing mmap MAP_FIXED
DEFINE_TEST(pessimistic_case) {
    current_mmap_impl = MMAP_IMPL(pessimistic_case);

    const struct region region = alloc_region(HEAP_START, 0);

    assert(region.addr == NULL);

    assert(test_pessimistic_case_mmap_counter == 2);
}

DEFINE_MMAP_IMPL(query_is_small) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);
    assert(length == REGION_MIN_SIZE);
    return MAP_FAILED;
}

// test with query = 0
DEFINE_TEST(query_is_zero) {
    current_mmap_impl = MMAP_IMPL(query_is_small);
    alloc_region(HEAP_START, 0);
}

// test with query > 0 and query < REGION_MIN_SIZE
DEFINE_TEST(query_is_small) {
    current_mmap_impl = MMAP_IMPL(query_is_small);
    alloc_region(HEAP_START, 42);
}

DEFINE_MMAP_IMPL(query_is_min_region_size) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);
    assert(length == (size_t) (REGION_MIN_SIZE + getpagesize()));
    return mmap(addr, length, prot, flags, fd, offset);
}

// test with query == REGION_MIN_SIZE (to check is block header size counted in mmap length)
DEFINE_TEST(query_is_min_region_size) {
    current_mmap_impl = MMAP_IMPL(query_is_min_region_size);

    const struct region region = alloc_region(HEAP_START, REGION_MIN_SIZE);

    assert(region.addr == HEAP_START);
    assert(region.size == (size_t) (REGION_MIN_SIZE + getpagesize()));
    assert(region.extends);

    struct block_header * block = region.addr;
    assert(block->next == NULL);
    assert(block->capacity.bytes == region.size - offsetof(struct block_header, contents));
    assert(block->is_free);

    memset(block->contents, 42, block->capacity.bytes);

    munmap(region.addr, region.size);
}

DEFINE_TEST_GROUP(query) {
    TEST_IN_GROUP(query_is_zero),
    TEST_IN_GROUP(query_is_small),
    TEST_IN_GROUP(query_is_min_region_size),
};

int main() {
    RUN_SINGLE_TEST(optimistic_case);
    RUN_SINGLE_TEST(map_fixed_failed);
    RUN_SINGLE_TEST(pessimistic_case);
    RUN_TEST_GROUP(query);
    return 0;
}
