#pragma once

#define PRI_SPECIFIER(e) (_Generic( (e), uint16_t : "%" PRIu16, uint32_t: "%" PRIu32, default: "NOT IMPLEMENTED" ))

_Noreturn void err( const char* msg, ... );
