#define TEST_SMART_MMAP

#include "test.h"

#include <assert.h>

#define BUFFER_SIZE 512


// test working with null argument
DEFINE_TEST(null) {
    _free(NULL);
}

// +-------------+
// | dirty block |--(next)->NULL
// +-------------+
DEFINE_TEST(next_is_null) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE }, NULL);
    struct block_header * const block = (void*) buffer;
    block->is_free = false;

    _free(block->contents);

    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

// +-------------+<-(space)->+-------+
// | dirty block |--(next)-->| block |
// +-------------+           +-------+
DEFINE_TEST(next_is_not_continuous) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE / 4 }, NULL);
    block->is_free = false;

    struct block_header * const next_block = (void*) (buffer + 3 * BUFFER_SIZE / 4);
    block_init(next_block, (block_size) { .bytes = BUFFER_SIZE / 4 }, NULL);

    block->next = next_block;

    _free(block->contents);

    assert(block->next == next_block);
    assert(block->capacity.bytes == BUFFER_SIZE / 4 - offsetof(struct block_header, contents));
    assert(block->is_free == true);

    assert(next_block->next == NULL);
    assert(next_block->capacity.bytes == BUFFER_SIZE / 4 - offsetof(struct block_header, contents));
    assert(next_block->is_free == true);
}

// +-------------+          +-------------+
// | dirty block |--(next)->| dirty block |
// +-------------+          +-------------+
DEFINE_TEST(next_block_is_dirty) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);
    block->is_free = false;

    struct block_header * const next_block = (void*) (buffer + BUFFER_SIZE / 2);
    block_init(next_block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);
    next_block->is_free = false;

    block->next = next_block;

    _free(block->contents);

    assert(block->next == next_block);
    assert(block->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(block->is_free == true);

    assert(next_block->next == NULL);
    assert(next_block->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(next_block->is_free == false);
}

// +-------------+          +-------+
// | dirty block |--(next)->| block |
// +-------------+          +-------+
DEFINE_TEST(merge) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);
    block->is_free = false;

    struct block_header * const next_block = (void*) (buffer + BUFFER_SIZE / 2);
    block_init(next_block, (block_size) { .bytes = BUFFER_SIZE / 4 }, NULL);

    block->next = next_block;

    _free(block->contents);

    assert(block->next == NULL);
    assert(block->capacity.bytes == 3 * BUFFER_SIZE / 4 - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

DEFINE_TEST_GROUP(next) {
    TEST_IN_GROUP(next_is_null),
    TEST_IN_GROUP(next_is_not_continuous),
    TEST_IN_GROUP(next_block_is_dirty),
};

int main() {
    RUN_SINGLE_TEST(null);
    RUN_TEST_GROUP(next);
    RUN_SINGLE_TEST(merge);
    return 0;
}
