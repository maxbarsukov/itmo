#define TEST_SMART_MMAP

#include "test.h"

#include <string.h>

#define BLOCK_SIZE 1024
#define BUFFER_SIZE (BLOCK_SIZE + REGION_MIN_SIZE)


static uint8_t buffer[BUFFER_SIZE] = { 0 };

static int test_mmap_counter = 0;

DEFINE_MMAP_IMPL(mmap_failed) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);

    assert(addr == buffer + BLOCK_SIZE);
    assert(length == REGION_MIN_SIZE);

    ++test_mmap_counter;
    return MAP_FAILED;
}

// test when mmap failed -> no changes in heap, returns NULL
DEFINE_TEST(mmap_failed) {
    current_mmap_impl = MMAP_IMPL(mmap_failed);

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BLOCK_SIZE }, NULL);

    test_mmap_counter = 0;
    struct block_header * const result = grow_heap(block, BLOCK_MIN_CAPACITY);
    assert(result == NULL);

    assert(test_mmap_counter == 2);

    assert(block->next == NULL);
    assert(block->capacity.bytes == BLOCK_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

DEFINE_MMAP_IMPL(mmap_fixed_failed) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);

    assert(length == REGION_MIN_SIZE);

    ++test_mmap_counter;
    if ((flags & MAP_FIXED) || (flags & MAP_FIXED_NOREPLACE)) {
        assert(addr == buffer + BLOCK_SIZE / 2);
        return MAP_FAILED;
    }

    return buffer + BLOCK_SIZE;
}

// test when mmap fixed failed -> only chain last with new, returns new region addr
DEFINE_TEST(mmap_fixed_failed) {
    current_mmap_impl = MMAP_IMPL(mmap_fixed_failed);

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BLOCK_SIZE / 2 }, NULL);

    test_mmap_counter = 0;
    struct block_header * const result = grow_heap(block, BLOCK_MIN_CAPACITY);
    assert((uint8_t*) result == buffer + BLOCK_SIZE);

    assert(test_mmap_counter == 2);

    assert(block->next == result);
    assert(block->capacity.bytes == BLOCK_SIZE / 2 - offsetof(struct block_header, contents));
    assert(block->is_free == true);

    assert(result->next == NULL);
    assert(result->capacity.bytes == REGION_MIN_SIZE - offsetof(struct block_header, contents));
    assert(result->is_free == true);
}

DEFINE_MMAP_IMPL(mmap_success) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);

    assert(addr == buffer + BLOCK_SIZE);
    assert(length == REGION_MIN_SIZE);
    assert((flags & MAP_FIXED) || (flags & MAP_FIXED_NOREPLACE));

    ++test_mmap_counter;
    return buffer + BLOCK_SIZE;
}

// test when mmap success and last is dirty -> only chain last with new, returns new region addr
DEFINE_TEST(mmap_success_last_dirty) {
    current_mmap_impl = MMAP_IMPL(mmap_success);

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BLOCK_SIZE }, NULL);
    block->is_free = false;

    test_mmap_counter = 0;
    struct block_header * const result = grow_heap(block, BLOCK_MIN_CAPACITY);
    assert((uint8_t*) result == buffer + BLOCK_SIZE);

    assert(test_mmap_counter == 1);

    assert(block->next == result);
    assert(block->capacity.bytes == BLOCK_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == false);

    assert(result->next == NULL);
    assert(result->capacity.bytes == REGION_MIN_SIZE - offsetof(struct block_header, contents));
    assert(result->is_free == true);
}

// test when mmap success and last is free -> merge last with new, returns old last
DEFINE_TEST(mmap_success_last_free) {
    current_mmap_impl = MMAP_IMPL(mmap_success);

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BLOCK_SIZE }, NULL);

    test_mmap_counter = 0;
    struct block_header * const result = grow_heap(block, BLOCK_MIN_CAPACITY);
    assert((uint8_t*) result == (uint8_t*) buffer);

    assert(test_mmap_counter == 1);

    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

DEFINE_TEST_GROUP(mmap_success) {
    TEST_IN_GROUP(mmap_success_last_dirty),
    TEST_IN_GROUP(mmap_success_last_free),
};

int main() {
    RUN_SINGLE_TEST(mmap_failed);
    RUN_SINGLE_TEST(mmap_fixed_failed);
    RUN_TEST_GROUP(mmap_success);
    return 0;
}
