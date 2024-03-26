#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)

int main() {
    int n, k;
    cin >> n >> k;

    int coords[n];
    repeat(n, i) cin >> coords[i];

    int left = 0;
    int right = coords[n - 1] - coords[0];

    while (left != right) {
        int mid = (left + right) / 2;

        int cows_count = 1;
        int last_cow_coord = coords[0];

        for (int coord : coords) {
            if (coord - last_cow_coord > mid) {
                last_cow_coord = coord;
                cows_count++;

                if (cows_count >= k) {
                    left = mid + 1;
                    break;
                }
            }
        }

        if (cows_count < k) right = mid;
    }

    cout << left;
    return 0;
}
