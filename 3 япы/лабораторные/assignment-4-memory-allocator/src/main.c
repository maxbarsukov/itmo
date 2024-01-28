#define _GNU_SOURCE

#include <assert.h>
#include <stdio.h>
#include <sys/mman.h>

#include "mem.h"
#include "mem_internals.h"

#define DEFAULT_HEAP_SIZE 4096
#define HEAP_START ((void*)0x04040000)

void debug(const char* fmt, ... );

#define get_header(mem) \
    ((struct block_header*) (((uint8_t*) (mem)) - offsetof(struct block_header, contents)))

void* map_pages(void const* addr, size_t length, int additional_flags) {
	return mmap((void*) addr,  length, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS | additional_flags , -1, 0);
}

void success_malloc(){
    debug("Test 1: success_malloc run...\n");

    debug("\nHeap initialization...\n", DEFAULT_HEAP_SIZE);
    void* heap = heap_init(DEFAULT_HEAP_SIZE);
    assert(heap);
    debug("Heap initialized: %d bytes\n", DEFAULT_HEAP_SIZE);

    debug("\n- 1: allocate some bytes\n");
    debug("Memory allocation: %d bytes...\n", 2048);
    void* allocated = _malloc(2048);
    assert(allocated);
    debug("Allocation sucess\n");

    debug("\n- 2: allocate overflow\n");
    debug("Memory allocation: %d bytes...\n", 8192);
    allocated = _malloc(8192);
    assert(allocated);
    debug("Allocation sucess\n");

    heap_term();
    debug("\nTest 1: passed!\n");
}

void free_block() {
    debug("\n\nTest 2: free_block run...\n");

    debug("\nHeap initialization...\n", 0);
    void* heap = heap_init(0);
    assert(heap);
    debug("Heap initialized: %d bytes\n", 0);

    debug("\nBlocks initialization...\n", 0);
    void* first_block = _malloc(10);
    void* second_block = _malloc(20);
    void* third_block = _malloc(30);

    assert(first_block);
    assert(second_block);
    assert(third_block);
    debug("\n3 blocks initialization success\n", 0);

    debug("\n- 1: free_second_block\n");
    _free(second_block);
    assert(!get_header(first_block)->is_free);
    assert(get_header(second_block)->is_free);
    assert(!get_header(third_block)->is_free);
    debug("\"free\" sucсess\n");

    heap_term();
    debug("\nTest 2: passed!\n");
}

void free_blocks() {
    debug("\n\nTest 3: free_blocks run...\n");

    debug("\nHeap initialization...\n", 0);
    void* heap = heap_init(0);
    assert(heap);
    debug("Heap initialized: %d bytes\n", 0);

    debug("\nBlocks initialization...\n", 0);
    void* first_block = _malloc(10);
    void* second_block = _malloc(20);
    void* third_block = _malloc(30);

    assert(first_block);
    assert(second_block);
    assert(third_block);
    debug("\n3 blocks initialization success\n", 0);

    debug("\n- 1: free_blocks\n");
    _free(first_block);
    _free(second_block);
    assert(get_header(first_block)->is_free);
    assert(get_header(second_block)->is_free);
    assert(!get_header(third_block)->is_free);
    debug("\"free\" sucсess\n");

    heap_term();
    debug("\nTest 3: passed!\n");
}

void expand_region() {
    debug("\n\nTest 4: expand_region...\n");

    debug("\nHeap initialization...\n", 0);
    struct region* heap = heap_init(0);
    assert(heap);
    debug("Heap initialized: %d bytes\n", 0);

    size_t initial_region_size = heap->size;

    debug("\nOverflow block initialization...\n", 0);
    _malloc(3 * 4096);
    size_t expanded_region_size = heap->size;
    debug("\nblock initialization success\n", 0);

    assert(initial_region_size < expanded_region_size);

    heap_term();
    debug("\nTest 4: passed!\n");
}

void expand_filled_region() {
    debug("\n\nTest 5: expand_filled_region...\n");

    debug("\nFilling memory...\n", 0);
    void* pre_allocated = map_pages(HEAP_START, 10, MAP_FIXED);
    assert(pre_allocated);
    debug("\nTrying to allocate in filled region...\n", 0);
    void* allocated_filled_ptr = _malloc(10);
    assert(allocated_filled_ptr);
    assert(pre_allocated != allocated_filled_ptr);

    heap_term();
    debug("\nTest 5: passed!\n");
}

int main() {
    success_malloc();
    free_block();
    free_blocks();
    expand_region();
    expand_filled_region();

    debug("\n\tSuccess!\n");
}
