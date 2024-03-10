#include <bits/stdc++.h>

#define repeat(x) for (int64_t i = (x); i--;)

using namespace std;

int main() {
    int64_t a, k, last_value = -1;
    uint16_t b, c, d;
    
    cin >> a >> b >> c >> d >> k;

    repeat(k) {
        a = a * b - c;

        if (a > d) {
            a = d;
        }

        if (a <= 0) {
            a = 0;
            break;
        }

        if (a == last_value) break;
        last_value = a;
    }

    cout << a << endl;

    return 0;
} 
