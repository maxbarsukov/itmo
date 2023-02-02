#include <stdio.h>

void conversion(int* first_elem_ref, int array_legth, int* result, int* result_code) {
    if (array_legth == 0) {
        *result_code = 0;
        *result = 0;
        return;
    }
    
    const int ASCII0 = 0x0030;
    const int ASCII9 = 0x0039;

    int arr[4] = {ASCII0, ASCII0, ASCII0, ASCII0};

    int i = 0;
    while(i - array_legth < 0) {
        const int value = *(first_elem_ref + i);
        
        if (value < ASCII0 || value > ASCII9) {
            *result_code = 1;
            return;
        }
        
        *(arr + i) = value - ASCII0;
        i++;
    }

    int res = *arr;
    const int pows10[4] = {1, 10, 100, 1000};

    i = 1;
    while (i < array_legth) {
        int temp = *(arr + i);
        int j = 0;
        while (j < temp) {
            res = res + *(pows10 + i);
            j++;
        }
        
        i++;
    }

    *result_code = 0;
    *result = res;
    return;
}

int main(int argc, char *argv[]) {
    int result = 0;
    int result_code = 0;

    enum { ARRAY_LENGTH = 4 };
    int a[ARRAY_LENGTH] = {0x32, 0x30, 0x38, 0x39};
    
    conversion(a, ARRAY_LENGTH, &result, &result_code);

    printf("RESULT CODE: %d\n", result_code);
    printf("RESULT: %d\n        0x%x\n", result, result);
    return 0;
}
