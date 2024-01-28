#define TEST_SMART_MMAP

#include "test.h"

#include <assert.h>

#define BUFFER_SIZE 1024


// test memalloc existing with big query
// +-------------+   +-------------+   +-+                      +-+   +-------------+
// | small block |-->| dirty block |-->| |->...small blocks...->| |-->| dirty block |
// +-------------+   +-------------+   +-+                      +-+   +-------------+
DEFINE_TEST(memalloc_existing_big_query) {
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

    struct block_header * const result = memalloc(BUFFER_SIZE / 2, block1);

    assert(result == block3);

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

// test memalloc existing with small query
// +-------------+   +-+                      +-+   +-------------+
// | dirty block |-->| |->...small blocks...->| |-->| dirty block |
// +-------------+   +-+                      +-+   +-------------+
DEFINE_TEST(memalloc_existing_small_query) {
    uint8_t buffer[BUFFER_SIZE] = { 0 };

    struct block_header * const block1 = (void*) (buffer + 0 * BUFFER_SIZE / 8);
    struct block_header * const block2 = (void*) (buffer + 1 * BUFFER_SIZE / 8);
    struct block_header * const block3 = (void*) (buffer + 2 * BUFFER_SIZE / 8);
    struct block_header * const block4 = (void*) (buffer + 3 * BUFFER_SIZE / 8);
    struct block_header * const block5 = (void*) (buffer + 4 * BUFFER_SIZE / 8);
    struct block_header * const block6 = (void*) (buffer + 5 * BUFFER_SIZE / 8);
    struct block_header * const block7 = (void*) (buffer + 6 * BUFFER_SIZE / 8);

    block_init(block7, (block_size) { .bytes = BUFFER_SIZE / 8 }, NULL);
    block_init(block6, (block_size) { .bytes = BUFFER_SIZE / 8 }, block7);
    block_init(block5, (block_size) { .bytes = BUFFER_SIZE / 8 }, block6);
    block_init(block4, (block_size) { .bytes = BUFFER_SIZE / 8 }, block5);
    block_init(block3, (block_size) { .bytes = BUFFER_SIZE / 8 }, block4);
    block_init(block2, (block_size) { .bytes = BUFFER_SIZE / 8 }, block3);
    block_init(block1, (block_size) { .bytes = BUFFER_SIZE / 8 }, block2);

    block1->is_free = false;
    block7->is_free = false;

    struct block_header * const result = memalloc(10, block1);

    assert(result == block2);

    struct block_header * const new_block = (void*) (block2->contents + BLOCK_MIN_CAPACITY);

    assert(block1->next == block2);
    assert(block1->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block1->is_free == false);

    assert(block2->next == new_block);
    assert(block2->capacity.bytes == BLOCK_MIN_CAPACITY);
    assert(block2->is_free == false);

    assert(new_block->next == block7);
    assert(new_block->capacity.bytes == 5 * BUFFER_SIZE / 8 - BLOCK_MIN_CAPACITY - 2 * offsetof(struct block_header, contents));
    assert(new_block->is_free == true);

    assert(block7->next == NULL);
    assert(block7->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block7->is_free == false);
}

static uint8_t buffer[BUFFER_SIZE] = { 0 };

static int test_mmap_counter = 0;
static size_t mmap_length = REGION_MIN_SIZE * 2;

DEFINE_MMAP_IMPL(mmap_failed) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);

    assert(addr == buffer + BUFFER_SIZE);
    assert(length == mmap_length);

    ++test_mmap_counter;
    return MAP_FAILED;
}

// test grow heap fail with big query (bigger than 2 pages)
// +-------------+   +-------------+   +-+                      +-+   +-------------+   +-------------+
// | small block |-->| dirty block |-->| |->...small blocks...->| |-->| dirty block |-->| small block |
// +-------------+   +-------------+   +-+                      +-+   +-------------+   +-------------+
DEFINE_TEST(grow_heap_fail_big_query) {
    current_mmap_impl = MMAP_IMPL(mmap_failed);

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

    mmap_length = REGION_MIN_SIZE + getpagesize();
    test_mmap_counter = 0;

    struct block_header * const result = memalloc(REGION_MIN_SIZE, block1);

    assert(result == NULL);
    assert(test_mmap_counter == 2);

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

// test grow heap fail with small query
// +-------------+
// | dirty block |
// +-------------+
DEFINE_TEST(grow_heap_fail_small_query) {
    current_mmap_impl = MMAP_IMPL(mmap_failed);

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE }, NULL);
    block->is_free = false;

    mmap_length = REGION_MIN_SIZE;
    test_mmap_counter = 0;

    struct block_header * const result = memalloc(10, block);

    assert(result == NULL);
    assert(test_mmap_counter == 2);

    assert(block->next == NULL);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == false);
}

static uint8_t mmap_buffer[REGION_MIN_SIZE] = { 0 };

DEFINE_MMAP_IMPL(mmap_fixed_failed) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);

    assert(length == REGION_MIN_SIZE);

    ++test_mmap_counter;
    if (test_mmap_counter == 1) {
        assert(addr == buffer + BUFFER_SIZE);
        return MAP_FAILED;
    }

    return mmap_buffer;
}

// TODO test grow heap success with big query
// +-------------+   +-------------+   +-+                      +-+   +-------------+   +-------------+
// | small block |-->| dirty block |-->| |->...small blocks...->| |-->| dirty block |-->| small block |
// +-------------+   +-------------+   +-+                      +-+   +-------------+   +-------------+
DEFINE_TEST(grow_heap_success_big_query) {
    current_mmap_impl = MMAP_IMPL(mmap_fixed_failed);

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

    test_mmap_counter = 0;
    struct block_header * const result = memalloc(BUFFER_SIZE / 2, block1);

    assert(result == (void*) mmap_buffer);
    assert(test_mmap_counter == 2);

    struct block_header * const new_block = (void*) (mmap_buffer + BUFFER_SIZE / 2 + offsetof(struct block_header, contents));

    assert(result->next == new_block);
    assert(result->capacity.bytes == BUFFER_SIZE / 2);
    assert(result->is_free == false);

    assert(new_block->next == NULL);
    assert(new_block->capacity.bytes == REGION_MIN_SIZE - BUFFER_SIZE / 2 - 2 * offsetof(struct block_header, contents));
    assert(new_block->is_free == true);

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

    assert(block8->next == result);
    assert(block8->capacity.bytes == BUFFER_SIZE / 8 - offsetof(struct block_header, contents));
    assert(block8->is_free == true);
}

// test grow heap success with small query
// +-------------+
// | dirty block |
// +-------------+
DEFINE_TEST(grow_heap_success_small_query) {
    current_mmap_impl = MMAP_IMPL(mmap_fixed_failed);

    struct block_header * const block = (void*) buffer;
    block_init(block, (block_size) { .bytes = BUFFER_SIZE }, NULL);
    block->is_free = false;

    mmap_length = REGION_MIN_SIZE;
    test_mmap_counter = 0;

    struct block_header * const result = memalloc(10, block);

    assert(result == (void*) mmap_buffer);
    assert(test_mmap_counter == 2);

    struct block_header * const new_block = (void*) (mmap_buffer + BLOCK_MIN_CAPACITY + offsetof(struct block_header, contents));

    assert(result->next == new_block);
    assert(result->capacity.bytes == BLOCK_MIN_CAPACITY);
    assert(result->is_free == false);

    assert(new_block->next == NULL);
    assert(new_block->capacity.bytes == REGION_MIN_SIZE - BLOCK_MIN_CAPACITY - 2 * offsetof(struct block_header, contents));
    assert(new_block->is_free == true);

    assert(block->next == result);
    assert(block->capacity.bytes == BUFFER_SIZE - offsetof(struct block_header, contents));
    assert(block->is_free == false);
}

DEFINE_TEST_GROUP(memalloc_existing) {
    TEST_IN_GROUP(memalloc_existing_big_query),
    TEST_IN_GROUP(memalloc_existing_small_query),
};

DEFINE_TEST_GROUP(grow_heap_fail) {
    TEST_IN_GROUP(grow_heap_fail_big_query),
    TEST_IN_GROUP(grow_heap_fail_small_query),
};

DEFINE_TEST_GROUP(grow_heap_success) {
    TEST_IN_GROUP(grow_heap_success_big_query),
    TEST_IN_GROUP(grow_heap_success_small_query),
};

int main() {
    RUN_TEST_GROUP(memalloc_existing);
    RUN_TEST_GROUP(grow_heap_fail);
    RUN_TEST_GROUP(grow_heap_success);
    return 0;
}
