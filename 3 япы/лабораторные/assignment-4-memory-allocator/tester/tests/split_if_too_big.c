#define TEST_SMART_MMAP

#include "test.h"

#include <assert.h>

#define BUFFER_SIZE 512
#define SMALL_BLOCK_SIZE (BLOCK_MIN_CAPACITY + offsetof(struct block_header, contents) / 2)


// test when block is not free
DEFINE_TEST(not_free) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE }, NULL);

    struct block_header * const block = (void*) buffer;
    block->is_free = false;

    assert(!split_if_too_big(block, BUFFER_SIZE - SMALL_BLOCK_SIZE - offsetof(struct block_header, contents)));
    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == false);
}

// test when block is not big enough
DEFINE_TEST(not_big_enough) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE }, NULL);

    struct block_header * const block = (void*) buffer;

    assert(!split_if_too_big(block, BUFFER_SIZE - SMALL_BLOCK_SIZE));
    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

// test when block is not big enough for min capacity
DEFINE_TEST(not_big_enough_for_min_capacity) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE }, NULL);

    struct block_header * const block = (void*) buffer;

    assert(!split_if_too_big(block, BUFFER_SIZE - SMALL_BLOCK_SIZE - offsetof(struct block_header, contents)));
    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

// test successful split
DEFINE_TEST(success) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE }, NULL);

    struct block_header * const block = (void*) buffer;

    assert(split_if_too_big(block, BLOCK_MIN_CAPACITY));

    struct block_header * const next_block = block->next;

    assert(next_block != NULL);
    assert(block->capacity.bytes == BLOCK_MIN_CAPACITY);
    assert(block->is_free == true);

    assert(next_block->next == NULL);
    assert(next_block->capacity.bytes == BUFFER_SIZE - 2 * offsetof(struct block_header, contents) - BLOCK_MIN_CAPACITY);
    assert(next_block->is_free == true);
}

DEFINE_TEST_GROUP(not_splittable) {
    TEST_IN_GROUP(not_free),
    TEST_IN_GROUP(not_big_enough),
    TEST_IN_GROUP(not_big_enough_for_min_capacity),
};

int main() {
    RUN_TEST_GROUP(not_splittable);
    RUN_SINGLE_TEST(success);
    return 0;
}
