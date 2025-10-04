#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <getopt.h>
#include <stdarg.h>
#include "common.h"
#include "pa1.h"
#include "ipc.h"

#define SAFE_FCLOSE(fp) \
    do                  \
    {                   \
        if (fp != NULL) \
        {               \
            fclose(fp); \
            fp = NULL;  \
        }               \
    } while (0)

#define INIT_HEADER(msg_var, type_enum, payload_ptr)                      \
    do                                                                    \
    {                                                                     \
        (msg_var).s_header.s_magic = MESSAGE_MAGIC;                       \
        (msg_var).s_header.s_type = (type_enum);                          \
        (msg_var).s_header.s_local_time = 0;                              \
        (msg_var).s_header.s_payload_len = (uint32_t)strlen(payload_ptr); \
    } while (0)

#define RECEIVE_ALL_MESSAGES(ctx, msg_var)                          \
    do                                                              \
    {                                                               \
        for (local_id idx = 1; idx < (local_id)(ctx)->total; ++idx) \
        {                                                           \
            if (idx != (ctx)->id)                                   \
            {                                                       \
                receive((ctx), idx, &(msg_var));                    \
            }                                                       \
        }                                                           \
    } while (0)

typedef struct
{
    local_id id;
    int total;
    int channels[MAX_PROCESS_ID][MAX_PROCESS_ID][2];
} ProcessData;

static ProcessData proc_ctx;
static FILE *evt_out = NULL;
static FILE *chan_out = NULL;

void build_channels(void)
{
    chan_out = fopen(pipes_log, "w");
    for (int src = 0; src < proc_ctx.total; ++src)
    {
        for (int dst = 0; dst < proc_ctx.total; ++dst)
        {
            if (src != dst)
            {
                if (pipe(proc_ctx.channels[src][dst]) == -1)
                {
                    perror("pipe");
                    exit(1);
                }
                if (chan_out != NULL)
                {
                    fprintf(chan_out, "%d -> %d: read=%d, write=%d\n",
                            src, dst, proc_ctx.channels[src][dst][0], proc_ctx.channels[src][dst][1]);
                }
            }
        }
    }
    SAFE_FCLOSE(chan_out);
}

void discard_unused_channels(void)
{
    for (int src = 0; src < proc_ctx.total; ++src)
    {
        for (int dst = 0; dst < proc_ctx.total; ++dst)
        {
            if (src != dst)
            {
                if (src != proc_ctx.id)
                    close(proc_ctx.channels[src][dst][0]);
                if (dst != proc_ctx.id)
                    close(proc_ctx.channels[src][dst][1]);
            }
        }
    }
}

void log_event(const char *format, ...)
{
    va_list list;
    va_start(list, format);
    vprintf(format, list);
    va_end(list);
    if (evt_out != NULL)
    {
        va_start(list, format);
        vfprintf(evt_out, format, list);
        va_end(list);
        fflush(evt_out);
    }
}

void run_child(void)
{
    Message msg_buf;
    memset(&msg_buf, 0, sizeof(Message));
    char payload_text[MAX_PAYLOAD_LEN];

    snprintf(payload_text, MAX_PAYLOAD_LEN, log_started_fmt, proc_ctx.id, getpid(), getppid());
    strcpy(msg_buf.s_payload, payload_text);
    INIT_HEADER(msg_buf, STARTED, payload_text);
    log_event(msg_buf.s_payload);
    send_multicast(&proc_ctx, &msg_buf);
    RECEIVE_ALL_MESSAGES(&proc_ctx, msg_buf);
    log_event(log_received_all_started_fmt, proc_ctx.id);

    snprintf(payload_text, MAX_PAYLOAD_LEN, log_done_fmt, proc_ctx.id);
    strcpy(msg_buf.s_payload, payload_text);
    INIT_HEADER(msg_buf, DONE, payload_text);
    log_event(msg_buf.s_payload);
    send_multicast(&proc_ctx, &msg_buf);
    RECEIVE_ALL_MESSAGES(&proc_ctx, msg_buf);
    log_event(log_received_all_done_fmt, proc_ctx.id);
}

int main(int argc, char *argv[])
{
    int nprocs = 0;
    int opt;

    while ((opt = getopt(argc, argv, "p:")) != -1)
    {
        if (opt == 'p')
            nprocs = atoi(optarg);
        else
        {
            fprintf(stderr, "Usage: %s -p X\n", argv[0]);
            return 1;
        }
    }

    if (nprocs <= 0 || nprocs >= MAX_PROCESS_ID)
    {
        return 1;
    }

    proc_ctx.total = nprocs + 1;
    evt_out = fopen(events_log, "w");
    build_channels();

    for (local_id idx = 1; idx <= nprocs; ++idx)
    {
        pid_t pid = fork();
        if (pid == -1)
        {
            perror("fork");
            return 1;
        }
        if (pid == 0)
        {
            proc_ctx.id = idx;
            discard_unused_channels();
            run_child();
            SAFE_FCLOSE(evt_out);
            return 0;
        }
    }

    proc_ctx.id = PARENT_ID;
    discard_unused_channels();

    Message parent_msg_buf;
    memset(&parent_msg_buf, 0, sizeof(Message));

    RECEIVE_ALL_MESSAGES(&proc_ctx, parent_msg_buf);
    log_event(log_received_all_started_fmt, PARENT_ID);
    RECEIVE_ALL_MESSAGES(&proc_ctx, parent_msg_buf);
    log_event(log_received_all_done_fmt, PARENT_ID);
    for (local_id idx = 1; idx < (local_id)proc_ctx.total; ++idx)
        wait(NULL);

    SAFE_FCLOSE(evt_out);
    return 0;
}
