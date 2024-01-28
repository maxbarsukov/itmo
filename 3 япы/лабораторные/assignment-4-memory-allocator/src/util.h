#ifndef _UTIL_H_
#define _UTIL_H_

#include <stddef.h>

inline size_t size_max( size_t x, size_t y ) { return (x >= y)? x : y ; }

_Noreturn void err( const char* msg, ... );


#endif
