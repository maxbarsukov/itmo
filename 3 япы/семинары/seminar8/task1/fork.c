#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <string.h>
#include <unistd.h>

#define ARR_SIZE 10

void* create_shared_memory(size_t size) {
    return mmap(NULL,
                size,
                PROT_READ | PROT_WRITE,
                MAP_SHARED | MAP_ANONYMOUS,
                -1, 0);
}

void print_array(int* arr, size_t size) {
    for (size_t i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
    puts("\n");
}

void change_array_element(int* arr, size_t size, size_t index, int new_value) {
    if (index < size) {
        arr[index] = new_value;
    }
}

int main(void) {
    int nums[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    int* shmem = create_shared_memory(sizeof(int) * 10);

    for (size_t i = 0; i < ARR_SIZE; i++) {
        shmem[i] = nums[i];
    }

    printf("Shared memory at: %p\n" , (void*)shmem);

    int pid = fork();
    if (pid == 0) {
        puts("[child] enter index and new value: ");

        size_t index;
        int num;
        scanf("%zu %d", &index, &num);

        change_array_element(shmem, ARR_SIZE, index, num);
        puts("[child] finishing child process...");
    } else {
        printf("[child] Child's pid is: %d\n", pid);
        puts("[parent] array before: ");
        print_array(shmem, ARR_SIZE);

        puts("[parent] wait for child process to dead");
        wait(NULL);

        puts("[parent] array after: ");
        print_array(shmem, ARR_SIZE);
    }
    return 0;
}
