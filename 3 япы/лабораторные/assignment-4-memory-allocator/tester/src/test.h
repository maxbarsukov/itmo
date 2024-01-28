#pragma once

#define _DEFAULT_SOURCE
#include <sys/mman.h>


static inline void * _mmap(void * addr, size_t length, int prot, int flags, int fd, off_t offset);


#define mmap _mmap
#undef _DEFAULT_SOURCE
#include "../../src/mem.c"
#undef mmap

#include "../../src/util.c"

#include "test_utils.h"
