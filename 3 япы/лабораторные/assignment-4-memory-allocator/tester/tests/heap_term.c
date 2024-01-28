#define TEST_SMART_MMAP

#include "test.h"

#include <fcntl.h>
#include <string.h>
#include <unistd.h>

#include <sys/mman.h>

static const size_t corner_cases[] = { 0, 10, 24, 24 + offsetof(struct block_header, contents),
    REGION_MIN_SIZE - offsetof(struct block_header, contents), REGION_MIN_SIZE };

static size_t test_length = 0;

DEFINE_MMAP_IMPL(success) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);
    return mmap(addr, length, prot, flags, fd, offset);
}

DEFINE_MMAP_IMPL(fail_fixed) {
    base_mmap_checks(addr, length, prot, flags, fd, offset);
    if (flags & (MAP_FIXED | MAP_FIXED_NOREPLACE))
        return MAP_FAILED;
    return mmap(0, length, prot, flags, fd, offset);
}

// test heap termination
DEFINE_TEST(term) {
    int maps_fd = open("/proc/self/maps", O_RDONLY, 0);
    assert(maps_fd > 0);
    const size_t PAGE_SIZE = 4096;
    uint8_t maps_before[PAGE_SIZE * 2];
    size_t maps_before_len = read(maps_fd, maps_before, PAGE_SIZE * 2 - 1);
    maps_before[PAGE_SIZE * 2 - 1] = '\0';
    close(maps_fd);
    assert(maps_before_len > 0);
    fprintf(stderr, "Intial maps:\n%s\n", (char *)maps_before);

    current_mmap_impl = MMAP_IMPL(success);
    assert(heap_init(0));
    assert(_malloc(REGION_MIN_SIZE - 2 * offsetof(struct block_header, contents) - BLOCK_MIN_CAPACITY));
    current_mmap_impl = MMAP_IMPL(fail_fixed);
    assert(_malloc(REGION_MIN_SIZE - offsetof(struct block_header, contents)));
    heap_term();

    maps_fd = open("/proc/self/maps", O_RDONLY, 0);
    assert(maps_fd > 0);
    uint8_t maps_after[PAGE_SIZE * 2];
    size_t maps_after_len = read(maps_fd, maps_after, PAGE_SIZE * 2 - 1);
    maps_after[PAGE_SIZE * 2 - 1] = '\0';
    close(maps_fd);
    assert(maps_after_len > 0);
    fprintf(stderr, "Final maps:\n%s\n", (char *)maps_after);
    assert(maps_before_len == maps_after_len);
    assert(memcmp(maps_before, maps_after, maps_before_len) == 0);
}

int main() {
    RUN_SINGLE_TEST(term);
    return 0;
}
