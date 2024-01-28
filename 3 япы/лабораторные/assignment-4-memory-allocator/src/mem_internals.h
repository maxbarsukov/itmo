#ifndef _MEM_INTERNALS_
#define _MEM_INTERNALS_

#include <inttypes.h>
#include <stdbool.h>
#include <stddef.h>

#define REGION_MIN_SIZE (2 * 4096)

struct region { void* addr; size_t size; bool extends; };
static const struct region REGION_INVALID = {0};

inline bool region_is_invalid( const struct region* r ) { return r->addr == NULL; }

typedef struct { size_t bytes; } block_capacity;
typedef struct { size_t bytes; } block_size;

struct block_header {
  struct block_header*    next;
  block_capacity capacity;
  bool           is_free;
  uint8_t        contents[];
};

inline block_size size_from_capacity( block_capacity cap ) { return (block_size) {cap.bytes + offsetof( struct block_header, contents ) }; }
inline block_capacity capacity_from_size( block_size sz ) { return (block_capacity) {sz.bytes - offsetof( struct block_header, contents ) }; }

#endif
