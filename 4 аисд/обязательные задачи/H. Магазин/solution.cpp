#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)

int main() {
    int n, k;
    cin >> n >> k;

    int elements[n];
    repeat(n, i) cin >> elements[i];

    sort(elements, elements + n, greater<int>());

    for (int i = k - 1; i <= n; i += k) elements[i] = 0;

    cout << accumulate(elements, elements + n, 0, plus<int>());
    return 0;
}
