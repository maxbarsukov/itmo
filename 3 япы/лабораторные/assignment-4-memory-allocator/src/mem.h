#ifndef _MEM_H_
#define _MEM_H_


#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>

#include <sys/mman.h>

#define HEAP_START ((void*)0x04040000)

void* _malloc( size_t query );
void  _free( void* mem );
void* heap_init( size_t initial_size );
void  heap_term( void );

#define DEBUG_FIRST_BYTES 4

void debug_struct_info( FILE* f, void const* address );
void debug_heap( FILE* f,  void const* ptr );

#endif
