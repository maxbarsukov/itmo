#ifndef __QUEUE_H__
#define __QUEUE_H__

typedef struct
{
    int priority;
    int data;
} QueueNode;

typedef struct
{
    QueueNode *nodes;
    int len;
    int size;
} PriorityQueue;

PriorityQueue *create_queue(int size);

void queue_push(PriorityQueue *pq, int priority, int data);

int queue_pop(PriorityQueue *pq);

int queue_peek(PriorityQueue *pq);

#endif
