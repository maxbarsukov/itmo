#include <bits/stdc++.h>

using namespace std;

int main() {
    uint64_t n;
    cin >> n;

    uint64_t  d_start = 1,
              d_end = 1,
              max_length = 0,
              cur_length,
              cur_start = 1;

    int64_t cur_value, last_value = -1, pre_last_value = -2;
    
    for (uint64_t i = 1; i <= n; ++i) {
        cin >> cur_value;

        if (cur_value == last_value && cur_value == pre_last_value && i > 2) {
            cur_start = i - 1;
        }

        cur_length = i - cur_start + 1;
        if (cur_length > max_length) {
            d_start = cur_start;
            d_end = i;

            max_length = cur_length;
        }

        pre_last_value = last_value;
        last_value = cur_value;
    }

    cout << d_start << " " << d_end << endl;

    return 0;
} 
