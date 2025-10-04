/**
 * @file     ipc.h
 * @Author   Michael Kosyakov and Evgeniy Ivanov (ifmo.distributedclass@gmail.com)
 * @date     March, 2014
 * @brief    A simple IPC library for programming assignments
 *
 * Students must not modify this file!
 */

#ifndef __IFMO_DISTRIBUTED_CLASS_IPC__H
#define __IFMO_DISTRIBUTED_CLASS_IPC__H

#include <stddef.h>
#include <stdint.h>

//------------------------------------------------------------------------------

typedef int8_t local_id;
typedef int16_t timestamp_t;

enum {
    MESSAGE_MAGIC = 0xAFAF,
    MAX_MESSAGE_LEN = 4096,
    PARENT_ID = 0,
    MAX_PROCESS_ID = 15
};

typedef enum {
    STARTED = 0,     ///< message with string (doesn't include trailing '\0')
    DONE,            ///< message with string (doesn't include trailing '\0')
    ACK,             ///< empty message
    STOP,            ///< empty message
    TRANSFER,        ///< message with TransferOrder
    BALANCE_HISTORY, ///< message with BalanceHistory
    CS_REQUEST,      ///< empty message
    CS_REPLY,        ///< empty message
    CS_RELEASE       ///< empty message
} MessageType;

typedef struct {
    uint16_t     s_magic;        ///< magic signature, must be MESSAGE_MAGIC
    uint16_t     s_payload_len;  ///< length of payload
    int16_t      s_type;         ///< type of the message
    timestamp_t  s_local_time;   ///< set by sender, depends on particular PA:
                                 ///< physical time in PA2 or Lamport's scalar
                                 ///< time in PA3
} __attribute__((packed)) MessageHeader;

enum {
    MAX_PAYLOAD_LEN = MAX_MESSAGE_LEN - sizeof(MessageHeader)
};

typedef struct {
    MessageHeader s_header;
    char s_payload[MAX_PAYLOAD_LEN]; ///< Must be used as a buffer, unused "tail"
                                     ///< shouldn't be transfered
} __attribute__((packed)) Message;

//------------------------------------------------------------------------------

/** Send a message to the process specified by id.
 *
 * @param self    Any data structure implemented by students to perform I/O
 * @param dst     ID of recepient
 * @param msg     Message to send
 *
 * @return 0 on success, any non-zero value on error
 */
int send(void * self, local_id dst, const Message * msg);

//------------------------------------------------------------------------------

/** Send multicast message.
 *
 * Send msg to all other processes including parrent.
 * Should stop on the first error.
 * 
 * @param self    Any data structure implemented by students to perform I/O
 * @param msg     Message to multicast.
 *
 * @return 0 on success, any non-zero value on error
 */
int send_multicast(void * self, const Message * msg);

//------------------------------------------------------------------------------

/** Receive a message from the process specified by id.
 *
 * Might block depending on IPC settings.
 *
 * @param self    Any data structure implemented by students to perform I/O
 * @param from    ID of the process to receive message from
 * @param msg     Message structure allocated by the caller
 *
 * @return 0 on success, any non-zero value on error
 */
int receive(void * self, local_id from, Message * msg);

//------------------------------------------------------------------------------

/** Receive a message from any process.
 *
 * Receive a message from any process, in case of blocking I/O should be used
 * with extra care to avoid deadlocks.
 *
 * @param self    Any data structure implemented by students to perform I/O
 * @param msg     Message structure allocated by the caller
 *
 * @return 0 on success, any non-zero value on error
 */
int receive_any(void * self, Message * msg);

//------------------------------------------------------------------------------

#endif // __IFMO_DISTRIBUTED_CLASS_IPC__H
