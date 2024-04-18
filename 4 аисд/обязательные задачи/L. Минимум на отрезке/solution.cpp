#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)

int main() {
    int n, k;
    cin >> n >> k;

    int numbers[n];
    repeat(n, i) cin >> numbers[i];

    deque<int> chunk;
    repeat(n, i) {
        if (!chunk.empty() && (i - chunk.front() >= k)) {
            chunk.pop_front();
        }
        while (!chunk.empty() && numbers[chunk.back()] >= numbers[i]) {
            chunk.pop_back();
        }

        chunk.push_back(i);

        if (i + 1 >= k) cout << numbers[chunk.front()] << " ";
    }

    return 0;
}
