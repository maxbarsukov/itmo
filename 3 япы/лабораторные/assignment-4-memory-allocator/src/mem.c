#define _DEFAULT_SOURCE

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "mem_internals.h"
#include "mem.h"
#include "util.h"

void debug_block(struct block_header* b, const char* fmt, ... );
void debug(const char* fmt, ... );

extern inline block_size size_from_capacity( block_capacity cap );
extern inline block_capacity capacity_from_size( block_size sz );
extern inline bool region_is_invalid( const struct region* r );

static bool   block_is_big_enough( size_t query, struct block_header* block ) { return block->capacity.bytes >= query; }
static size_t pages_count( size_t mem ) { return mem / getpagesize() + ((mem % getpagesize()) > 0); }
static size_t round_pages( size_t mem ) { return getpagesize() * pages_count( mem ); }


static void block_init( void* restrict addr, block_size block_sz, void* restrict next ) {
  *((struct block_header*)addr) = (struct block_header) {
    .next = next,
    .capacity = capacity_from_size(block_sz),
    .is_free = true
  };
}

static size_t region_actual_size(size_t query) {
  return size_max(round_pages(query), REGION_MIN_SIZE);
}

static void* map_pages(void const* addr, size_t length, int additional_flags) {
  return mmap((void*) addr, length, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS | additional_flags , -1, 0);
}

/*  аллоцировать регион памяти и инициализировать его блоком */
static struct region alloc_region( void const * addr, size_t query ) {
  const size_t actual_size = region_actual_size(size_from_capacity ((block_capacity){ .bytes = query }).bytes);

  void* region_addr = map_pages(addr, actual_size, MAP_FIXED_NOREPLACE);
  if (region_addr == MAP_FAILED) {
    if ((region_addr = map_pages(addr, actual_size, 0)) == MAP_FAILED) return REGION_INVALID;
  }

  block_init(region_addr, (block_size) { .bytes = actual_size }, NULL);
  return (struct region) {
    .addr = region_addr,
    .size = actual_size,
    .extends = (region_addr == addr),
  };
}

#define BLOCK_MIN_CAPACITY 24

/*  --- Разделение блоков (если найденный свободный блок слишком большой )--- */

static bool block_splittable( struct block_header* restrict block, size_t query) {
  return block->is_free && query + offsetof( struct block_header, contents ) + BLOCK_MIN_CAPACITY <= block->capacity.bytes;
}

static bool split_if_too_big( struct block_header* block, size_t query ) {
  if (!block || !block_splittable(block, query)) return false;

  query = size_max(query, BLOCK_MIN_CAPACITY);

  const block_capacity original_bc = (block_capacity) { .bytes = query };
  const block_size original_bs = size_from_capacity(original_bc);

  const block_capacity new_bc = (block_capacity) {
    .bytes = block->capacity.bytes - original_bs.bytes,
  };

  const block_size new_bs = size_from_capacity(new_bc);

  block_init(
    (void *)block + original_bs.bytes,
    new_bs,
    block->next
  );

  block->capacity = original_bc;
  block->next = (void *)((uint8_t *)block + original_bs.bytes);
  return true;
}


/*  --- Слияние соседних свободных блоков --- */

static void* block_after( struct block_header const* block ) {
  return  (void*) (block->contents + block->capacity.bytes);
}

static bool blocks_continuous(
                               struct block_header const* fst,
                               struct block_header const* snd ) {
  return (void*)snd == block_after(fst);
}

static bool mergeable(struct block_header const* restrict fst, struct block_header const* restrict snd) {
  return fst->is_free && snd->is_free && blocks_continuous(fst, snd);
}

static bool try_merge_with_next( struct block_header* block ) {
  const struct block_header *next_block = block->next;

  if (!block || !next_block) return false;
  if (!mergeable(block, next_block)) return false;

  block_size merged_size = (block_size) {
    .bytes = size_from_capacity(block->capacity).bytes + size_from_capacity(next_block->capacity).bytes
  };

  block_init(block, merged_size, next_block->next);
  return true;
}

/*  --- ... ecли размера кучи хватает --- */
struct block_search_result {
  enum {BSR_FOUND_GOOD_BLOCK, BSR_REACHED_END_NOT_FOUND, BSR_CORRUPTED} type;
  struct block_header* block;
};


static struct block_search_result find_good_or_last( struct block_header* restrict block, size_t sz )    {
  if (!block) return (struct block_search_result) { .type = BSR_CORRUPTED };

  for (;block; block = block->next) {
    if (block->is_free) {
      while (try_merge_with_next(block));

      if (block_is_big_enough(sz, block)) {
        return (struct block_search_result) {.block = block, .type = BSR_FOUND_GOOD_BLOCK};
      }
    }

    if (block->next == NULL) break;
  }

  return (struct block_search_result) {.block = block, .type = BSR_REACHED_END_NOT_FOUND};
}

/*  Попробовать выделить память в куче начиная с блока `block` не пытаясь расширить кучу
 Можно переиспользовать как только кучу расширили. */
static struct block_search_result try_memalloc_existing( size_t query, struct block_header* block ) {
  struct block_search_result new_block_result = find_good_or_last(block, query);

  if (new_block_result.type == BSR_CORRUPTED || new_block_result.type == BSR_REACHED_END_NOT_FOUND) {
    return new_block_result;
  }

  split_if_too_big(new_block_result.block, query);
  new_block_result.block->is_free = false;

  return new_block_result;
}

static struct block_header* grow_heap( struct block_header* restrict last, size_t query ) {
  if (last == NULL) return NULL;

  struct region new_region = alloc_region(block_after(last), query);
  if (region_is_invalid(&new_region)) return NULL;

  last->next = new_region.addr;

  if (new_region.extends && try_merge_with_next(last)) return last;
  return last->next;
}

/*  Реализует основную логику malloc и возвращает заголовок выделенного блока */
static struct block_header* memalloc( size_t query, struct block_header* heap_start) {
  if (heap_start == NULL) return NULL;

  query = size_max(query, BLOCK_MIN_CAPACITY);
  struct block_search_result result = try_memalloc_existing(query, heap_start);

  if (result.type == BSR_CORRUPTED)        return NULL;
  if (result.type == BSR_FOUND_GOOD_BLOCK) return result.block;

  struct block_header *suitable = grow_heap(result.block, query);

  if (!suitable) return NULL;
  return try_memalloc_existing(query, suitable).block;
}

void* _malloc( size_t query ) {
  struct block_header* const addr = memalloc( query, (struct block_header*) HEAP_START );
  if (addr) return addr->contents;
  else return NULL;
}

void* heap_init(size_t initial) {
  const struct region region = alloc_region(HEAP_START, initial);
  if (region_is_invalid(&region)) return NULL;

  return region.addr;
}

/*  освободить всю память, выделенную под кучу */
void heap_term() {
  struct block_header *current = (struct block_header*) HEAP_START;

  while (current != NULL) {
    struct block_header *next = current->next;

    block_size to_free = size_from_capacity(current->capacity);
    for(; next && blocks_continuous(current, next); next = next->next) {
      to_free.bytes += size_from_capacity(next->capacity).bytes;
    }

    munmap(current, to_free.bytes);
    current = next;
  }
}

static struct block_header* block_get_header(void* contents) {
  return (struct block_header*) (((uint8_t*)contents)-offsetof(struct block_header, contents));
}

void _free( void* mem ) {
  if (!mem) return;
  struct block_header* header = block_get_header( mem );
  header->is_free = true;
  while (try_merge_with_next (header));
}
