#define TEST_SMART_MMAP

#include "test.h"

#include <assert.h>

#define BUFFER_SIZE 1024


// +-------------+   +-------------+   +-+                      +-+   +-------------+
// | small block |-->| dirty block |-->| |->...small blocks...->| |-->| dirty block |-->...
// +-------------+   +-------------+   +-+                      +-+   +-------------+
DEFINE_TEST(fail) {
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

    const struct block_search_result bsr = try_memalloc_existing(BUFFER_SIZE / 2, block1);

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
DEFINE_TEST(split) {
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

    const struct block_search_result bsr = try_memalloc_existing(BUFFER_SIZE / 2, block1);

    assert(bsr.type == BSR_FOUND_GOOD_BLOCK);
    assert(bsr.block == block3);

    struct block_header * const new_block = (void*) (block3->contents + BUFFER_SIZE / 2);

    assert(block1->next == block2);
    assert(block1->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block1->is_free == true);

    assert(block2->next == block3);
    assert(block2->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block2->is_free == false);

    assert(block3->next == new_block);
    assert(block3->capacity.bytes == BUFFER_SIZE / 2);
    assert(block3->is_free == false);

    assert(new_block->next == block8);
    assert(new_block->capacity.bytes == BUFFER_SIZE / 8 - 2 * offsetof(struct block_header, contents));
    assert(new_block->is_free == true);

    assert(block8->next == NULL);
    assert(block8->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block8->is_free == false);
}

int main() {
    RUN_SINGLE_TEST(fail);
    RUN_SINGLE_TEST(split);
    return 0;
}
