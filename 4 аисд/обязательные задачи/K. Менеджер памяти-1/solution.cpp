#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int(i) = 0; (i) < (times); (i)++)
#define loop for (;;)
#define unless(cond) if (!(cond))

typedef int block_index;

enum class Status {
    Declined = 0,
    Allocated = 1,
    Removed = 2
};


class Block {
public:
    bool is_free;
    int start, end;

    block_index index;
    
    Block *prev, *next;

    Block(Block *prev, Block *next, bool free, int start, int end, block_index index) {
        this->is_free = free;
        this->start = start;
        this->end = end;

        this->index = index;
        this->prev = prev;
        this->next = next;

        if (prev) prev->next = this;
        if (next) next->prev = this;
    }

    void remove() {
        if (prev) prev->next = next;
        if (next) next->prev = prev;
    }

    int size() {
        return this->end - this->start;
    }
};


class Heap {
public:
    int current_request;
    int heap_size;

    vector<Status> request_status;
    vector<Block*> heap;
    vector<Block*> blocks_for_requests;

    Heap(int n, int m) {
        current_request = 0;

        request_status.resize(m);
        blocks_for_requests.resize(m);

        heap.resize(m);
        heap_size = 1;
        heap[0] = new Block(nullptr, nullptr, true, 0, n, 0);
    }

    void allocate(int request_size) {
        Block *root = heap[0];

        if (heap_size == 0 || (root->size() < request_size)) {
            request_status[current_request++] = Status::Declined;
            cout << "-1" << endl;
            return;
        }

        request_status[current_request++] = Status::Allocated;
        blocks_for_requests[current_request - 1] = new Block(root->prev, root, false, root->start, root->start + request_size, -1);

        cout << root->start + 1 << endl;

        root->start += request_size;
        if (root->start < root->end) {
            heapify(root->index);
        } else {
            root->remove();
            pop();
            delete(root);
        }
    }

    void free(int request_index) {
        request_index--;

        request_status[current_request++] = Status::Removed;

        if (request_status[request_index] == Status::Declined) return;

        request_status[request_index] = Status::Removed;

        Block *block = blocks_for_requests[request_index];
        Block *prev_block = block->prev;
        Block *next_block = block->next;

        unless ((prev_block && prev_block->is_free) || (next_block && next_block->is_free)) {
            block->is_free = true;
            block->index = heap_size;
            heap[heap_size] = block;
            lift(heap_size++);
            return;
        }
        unless (prev_block && prev_block->is_free) {
            next_block->start = block->start;
            lift(next_block->index);
            block->remove();
            delete(block);
            return;
        }
        unless (next_block && next_block->is_free) {
            prev_block->end = block->end;
            lift(prev_block->index);
            block->remove();
            delete(block);
            return;
        }

        prev_block->end = next_block->end;
        lift(prev_block->index);

        block->remove();
        delete(block);

        remove(next_block->index);
        next_block->remove();
        delete(next_block);
    }

    void dispatch(int request) {
        if (request > 0) {
            this->allocate(abs(request));
        } else {
            this->free(abs(request));
        }
    }

private:
    block_index get_parent_index(block_index index) {
        return (index - 1) / 2;
    }

    block_index get_left_child_index(block_index index) {
        return 2 * index + 1;
    }

    block_index get_right_child_index(block_index index) {
        return 2 * index + 2;
    }

    void swap(block_index index1, block_index index2) {
        std::swap(heap[index1], heap[index2]);
        heap[index1]->index = index1;
        heap[index2]->index = index2;
    }

    bool better(block_index index1, block_index index2) {
        return heap[index1]->size() > heap[index2]->size();
    }

    void heapify(block_index index) {
        loop {
            block_index largest = index;
            block_index left_child = get_left_child_index(index);
            block_index right_child = get_right_child_index(index);

            if ((left_child < heap_size) && better(left_child, largest))
                largest = left_child;
            if ((right_child < heap_size) && better(right_child, largest))
                largest = right_child;
            if (index == largest) return;

            swap(index, largest);
            heapify(largest);
        }
    }

    void pop() {
        heap_size--;
        unless (heap_size == 0) {
            swap(0, heap_size);
            heapify(0);
        }
    }

    void lift(block_index index) {
        while (index && better(index, get_parent_index(index))) {
            swap(index, get_parent_index(index));
            index = get_parent_index(index);
        }
    }

    void remove(block_index index) {
        swap(index, heap_size - 1);
        heap_size--;
        if (index < heap_size) {
            lift(index);
            heapify(index);
        }
    }
};


int main() {
    ios::sync_with_stdio(0);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n, m, request;
    cin >> n >> m;

    Heap* heap = new Heap(n, m);
    repeat(m, i) {
        cin >> request;
        heap->dispatch(request);
    }

    delete(heap);
    return 0;
}
