#include <stdlib.h>
#include <stdio.h>

extern void print_string(char*);
extern void print_file(char*);

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <pathname>\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    print_file(argv[1]);
    return 0;
}
