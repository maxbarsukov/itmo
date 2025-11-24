#ifndef __LAMPORT_TIME_H__
#define __LAMPORT_TIME_H__

#include <stdint.h>
#include "ipc.h"

timestamp_t get_lamport_time(void);

void lamport_increment(void);

void lamport_update_from(timestamp_t received_time);

#endif
