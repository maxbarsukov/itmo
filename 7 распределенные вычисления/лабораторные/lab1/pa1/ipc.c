#include <unistd.h>
#include <string.h>
#include "ipc.h"
#include "common.h"

#define HDR_SIZE (sizeof(MessageHeader))

typedef struct
{
    local_id id;
    int total;
    int channels[MAX_PROCESS_ID][MAX_PROCESS_ID][2];
} ProcessData;

int send(void *self, local_id dst, const Message *msg)
{
    ProcessData *data = (ProcessData *)self;
    int wr_fd = data->channels[data->id][dst][1];
    size_t full = HDR_SIZE + msg->s_header.s_payload_len;
    ssize_t sent = write(wr_fd, msg, full);
    return (sent == (ssize_t)full) ? 0 : -1;
}

int send_multicast(void *self, const Message *msg)
{
    ProcessData *data = (ProcessData *)self;
    for (local_id idx = 0; idx < (local_id)data->total; ++idx)
    {
        if (idx != data->id)
        {
            if (send(self, idx, msg) != 0)
                return -1;
        }
    }
    return 0;
}

int receive(void *self, local_id from, Message *msg)
{
    ProcessData *data = (ProcessData *)self;
    int rd_fd = data->channels[from][data->id][0];
    if (read(rd_fd, &msg->s_header, HDR_SIZE) != HDR_SIZE)
        return -1;
    if (msg->s_header.s_payload_len > 0)
        read(rd_fd, msg->s_payload, msg->s_header.s_payload_len);
    return 0;
}

int receive_any(void *self, Message *msg)
{
    ProcessData *data = (ProcessData *)self;
    for (local_id idx = 0; idx < (local_id)data->total; ++idx)
    {
        if (idx != data->id)
        {
            int rd_fd = data->channels[idx][data->id][0];
            if (read(rd_fd, &msg->s_header, HDR_SIZE) == HDR_SIZE)
            {
                if (msg->s_header.s_payload_len > 0)
                    read(rd_fd, msg->s_payload, msg->s_header.s_payload_len);

                return 0;
            }
        }
    }
    return -1;
}
