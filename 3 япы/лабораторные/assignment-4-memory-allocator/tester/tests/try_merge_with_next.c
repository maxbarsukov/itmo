#define TEST_SMART_MMAP

#include "test.h"

#include <assert.h>

#define BUFFER_SIZE 512


// +-------+
// | block |--(next)->NULL
// +-------+
DEFINE_TEST(next_is_null) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE }, NULL);
    struct block_header * const block = (void*) buffer;

    assert(!try_merge_with_next(block));

    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

// +-------+<-(space)->+-------+
// | block |--(next)-->| block |
// +-------+           +-------+
DEFINE_TEST(next_is_not_continuous) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE / 4 }, NULL);

    struct block_header * const next_block = (void*) (buffer + 3 * BUFFER_SIZE / 4);
    block_init(next_block, (block_size) { .bytes = BUFFER_SIZE / 4 }, NULL);

    block->next = next_block;

    assert(!try_merge_with_next(block));

    assert(block->next == next_block);
    assert(block->capacity.bytes == BUFFER_SIZE / 4 - offsetof(struct block_header, contents));
    assert(block->is_free == true);

    assert(next_block->next == NULL);
    assert(next_block->capacity.bytes == BUFFER_SIZE / 4 - offsetof(struct block_header, contents));
    assert(next_block->is_free == true);
}

// +-------------+          +-------+
// | dirty block |--(next)->| block |
// +-------------+          +-------+
DEFINE_TEST(block_is_dirty) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);
    block->is_free = false;

    struct block_header * const next_block = (void*) (buffer + BUFFER_SIZE / 2);
    block_init(next_block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);

    block->next = next_block;

    assert(!try_merge_with_next(block));

    assert(block->next == next_block);
    assert(block->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(block->is_free == false);

    assert(next_block->next == NULL);
    assert(next_block->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(next_block->is_free == true);
}

// +-------+          +-------------+
// | block |--(next)->| dirty block |
// +-------+          +-------------+
DEFINE_TEST(next_block_is_dirty) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);

    struct block_header * const next_block = (void*) (buffer + BUFFER_SIZE / 2);
    block_init(next_block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);
    next_block->is_free = false;

    block->next = next_block;

    assert(!try_merge_with_next(block));

    assert(block->next == next_block);
    assert(block->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(block->is_free == true);

    assert(next_block->next == NULL);
    assert(next_block->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(next_block->is_free == false);
}

// +-------------+          +-------------+
// | dirty block |--(next)->| dirty block |
// +-------------+          +-------------+
DEFINE_TEST(both_blocks_are_dirty) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);
    block->is_free = false;

    struct block_header * const next_block = (void*) (buffer + BUFFER_SIZE / 2);
    block_init(next_block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);
    next_block->is_free = false;

    block->next = next_block;

    assert(!try_merge_with_next(block));

    assert(block->next == next_block);
    assert(block->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(block->is_free == false);

    assert(next_block->next == NULL);
    assert(next_block->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(next_block->is_free == false);
}

// +-------+          +-------+
// | block |--(next)->| block |
// +-------+          +-------+
DEFINE_TEST(success) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE / 2 }, NULL);

    struct block_header * const next_block = (void*) (buffer + BUFFER_SIZE / 2);
    block_init(next_block, (block_size) { .bytes = BUFFER_SIZE / 4 }, NULL);

    // bad block address to check that merging is not acting with block->next->next
    struct block_header * const next_next_block = (void*) 0x1488; // really bad address ._.

    block->next = next_block;
    next_block->next = next_next_block;

    assert(try_merge_with_next(block));

    assert(block->next == next_next_block);
    assert(block->capacity.bytes == 3 * BUFFER_SIZE / 4 - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

DEFINE_TEST_GROUP(next) {
    TEST_IN_GROUP(next_is_null),
    TEST_IN_GROUP(next_is_not_continuous),
};

DEFINE_TEST_GROUP(dirtiness) {
    TEST_IN_GROUP(block_is_dirty),
    TEST_IN_GROUP(next_block_is_dirty),
    TEST_IN_GROUP(both_blocks_are_dirty),
};

int main() {
    RUN_TEST_GROUP(next);
    RUN_TEST_GROUP(dirtiness);
    RUN_SINGLE_TEST(success);
    return 0;
}
