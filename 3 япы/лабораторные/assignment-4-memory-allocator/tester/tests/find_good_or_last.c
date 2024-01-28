#define TEST_SMART_MMAP

#include "test.h"

#include <assert.h>

#define BUFFER_SIZE 1024


// +------------+
// | good block |-->NULL
// +------------+
DEFINE_TEST(single_block_is_good) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE }, NULL);
    struct block_header * const block = (void*) buffer;

    const struct block_search_result bsr = find_good_or_last(block, BUFFER_SIZE / 2);

    assert(bsr.type == BSR_FOUND_GOOD_BLOCK);
    assert(bsr.block == block);

    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

// +-------------+
// | dirty block |-->NULL
// +-------------+
DEFINE_TEST(single_block_is_dirty) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE }, NULL);
    struct block_header * const block = (void*) buffer;
    block->is_free = false;

    const struct block_search_result bsr = find_good_or_last(block, BUFFER_SIZE / 2);

    assert(bsr.type == BSR_REACHED_END_NOT_FOUND);
    assert(bsr.block == block);

    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == false);
}

// +-------------+
// | small block |-->NULL
// +-------------+
DEFINE_TEST(single_block_is_small) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    block_init(buffer, (block_size) { .bytes = BUFFER_SIZE / 4 }, NULL);
    struct block_header * const block = (void*) buffer;

    const struct block_search_result bsr = find_good_or_last(block, BUFFER_SIZE / 2);

    assert(bsr.type == BSR_REACHED_END_NOT_FOUND);
    assert(bsr.block == block);

    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE / 4 - offsetof(struct block_header, contents));
    assert(block->is_free == true);
}

// +-------------+   +-------------+   +-+                      +-+   +-------------+
// | small block |-->| dirty block |-->| |->...small blocks...->| |-->| dirty block |-->...
// +-------------+   +-------------+   +-+                      +-+   +-------------+
DEFINE_TEST(multiple_blocks_fail) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block1 = (void*) (buffer + 0 * BUFFER_SIZE / 8);
    struct block_header * const block2 = (void*) (buffer + 1 * BUFFER_SIZE / 8);
    struct block_header * const block3 = (void*) (buffer + 2 * BUFFER_SIZE / 8);
    struct block_header * const block4 = (void*) (buffer + 3 * BUFFER_SIZE / 8);
    struct block_header * const block5 = (void*) (buffer + 4 * BUFFER_SIZE / 8);
    struct block_header * const block6 = (void*) (buffer + 5 * BUFFER_SIZE / 8);
    struct block_header * const block7 = (void*) (buffer + 6 * BUFFER_SIZE / 8);
    struct block_header * const block8 = (void*) (buffer + 7 * BUFFER_SIZE / 8);

    block_init(block8, (block_size) { .bytes = BUFFER_SIZE / 8 }, NULL);
    block_init(block7, (block_size) { .bytes = BUFFER_SIZE / 8 }, block8);
    block_init(block6, (block_size) { .bytes = BUFFER_SIZE / 8 }, block7);
    block_init(block5, (block_size) { .bytes = BUFFER_SIZE / 8 }, block6);
    block_init(block4, (block_size) { .bytes = BUFFER_SIZE / 8 }, block5);
    block_init(block3, (block_size) { .bytes = BUFFER_SIZE / 8 }, block4);
    block_init(block2, (block_size) { .bytes = BUFFER_SIZE / 8 }, block3);
    block_init(block1, (block_size) { .bytes = BUFFER_SIZE / 8 }, block2);

    block2->is_free = false;
    block7->is_free = false;

    const struct block_search_result bsr = find_good_or_last(block1, BUFFER_SIZE / 2);

    assert(bsr.type == BSR_REACHED_END_NOT_FOUND);
    assert(bsr.block == block8);

    assert(block1->next == block2);
    assert(block1->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block1->is_free == true);

    assert(block2->next == block3);
    assert(block2->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block2->is_free == false);

    assert(block3->next == block7);
    assert(block3->capacity.bytes == BUFFER_SIZE / 2 - offsetof(struct block_header, contents));
    assert(block3->is_free == true);

    assert(block7->next == block8);
    assert(block7->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block7->is_free == false);

    assert(block8->next == NULL);
    assert(block8->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block8->is_free == true);
}

// +-------------+   +-------------+   +-+                      +-+   +-------------+
// | small block |-->| dirty block |-->| |->...small blocks...->| |-->| dirty block |
// +-------------+   +-------------+   +-+                      +-+   +-------------+
DEFINE_TEST(multiple_blocks_success) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block1 = (void*) (buffer + 0 * BUFFER_SIZE / 8);
    struct block_header * const block2 = (void*) (buffer + 1 * BUFFER_SIZE / 8);
    struct block_header * const block3 = (void*) (buffer + 2 * BUFFER_SIZE / 8);
    struct block_header * const block4 = (void*) (buffer + 3 * BUFFER_SIZE / 8);
    struct block_header * const block5 = (void*) (buffer + 4 * BUFFER_SIZE / 8);
    struct block_header * const block6 = (void*) (buffer + 5 * BUFFER_SIZE / 8);
    struct block_header * const block7 = (void*) (buffer + 6 * BUFFER_SIZE / 8);
    struct block_header * const block8 = (void*) (buffer + 7 * BUFFER_SIZE / 8);

    block_init(block8, (block_size) { .bytes = BUFFER_SIZE / 8 }, NULL);
    block_init(block7, (block_size) { .bytes = BUFFER_SIZE / 8 }, block8);
    block_init(block6, (block_size) { .bytes = BUFFER_SIZE / 8 }, block7);
    block_init(block5, (block_size) { .bytes = BUFFER_SIZE / 8 }, block6);
    block_init(block4, (block_size) { .bytes = BUFFER_SIZE / 8 }, block5);
    block_init(block3, (block_size) { .bytes = BUFFER_SIZE / 8 }, block4);
    block_init(block2, (block_size) { .bytes = BUFFER_SIZE / 8 }, block3);
    block_init(block1, (block_size) { .bytes = BUFFER_SIZE / 8 }, block2);

    block2->is_free = false;
    block8->is_free = false;

    const struct block_search_result bsr = find_good_or_last(block1, BUFFER_SIZE / 2);

    assert(bsr.type == BSR_FOUND_GOOD_BLOCK);
    assert(bsr.block == block3);

    assert(block1->next == block2);
    assert(block1->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block1->is_free == true);

    assert(block2->next == block3);
    assert(block2->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block2->is_free == false);

    assert(block3->next == block8);
    assert(block3->capacity.bytes == 5 * BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block3->is_free == true);

    assert(block8->next == NULL);
    assert(block8->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block8->is_free == false);
}

DEFINE_TEST_GROUP(single_block) {
    TEST_IN_GROUP(single_block_is_good),
    TEST_IN_GROUP(single_block_is_dirty),
    TEST_IN_GROUP(single_block_is_small),
};

DEFINE_TEST_GROUP(multiple_blocks) {
    TEST_IN_GROUP(multiple_blocks_fail),
    TEST_IN_GROUP(multiple_blocks_success),
};

int main() {
    RUN_TEST_GROUP(single_block);
    RUN_TEST_GROUP(multiple_blocks);
    return 0;
}
