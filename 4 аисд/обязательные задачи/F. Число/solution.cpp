#include <bits/stdc++.h>

using namespace std;

int main() {
    auto compare = [](const string& str1, const string& str2) { return str1 + str2 > str2 + str1; }; 

    multiset<string, decltype(compare)> data(compare); 

    for (string input; cin >> input;) data.insert(input);
    for (const string& str: data) cout << str;

    return 0;
}
