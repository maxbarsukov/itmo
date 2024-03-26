#include <bits/stdc++.h>

using namespace std;

vector<pair<char, int>> weights;
map<char, int> char_count;

int main() {
    string s;
    cin >> s;

    for (int cur_weight, i = 0; i < 26; i++) {
        cin >> cur_weight;
        weights.push_back(make_pair('a' + i, cur_weight));
    }

    sort(weights.begin(), weights.end(),
        [](const pair<char, int> &a, const pair<char, int> &b) {
            return a.second > b.second;
        }
    );

    for (char& c : s) char_count[c]++;

    int ptr_after_duos = count_if(char_count.begin(), char_count.end(),
        [](pair<char, int> p) { return p.second >= 2; }
    );

    int start = 0;
    int end = s.length() - 1;

    char result[s.length()];
    result[s.length()] = 0;
    for (auto it : weights) {
        char c = it.first;

        if (char_count[c] >= 2) {
            result[start] = c;
            result[end] = c;

            char_count[c] -= 2;
            start++;
            end--;
        }

        for (; char_count[c] > 0; char_count[c]--) {
            result[ptr_after_duos] = c;
            ptr_after_duos++;
        }
    }

    printf("%s", result);
    return 0;
}
