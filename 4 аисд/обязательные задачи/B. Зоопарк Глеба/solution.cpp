#include <bits/stdc++.h>

using namespace std;

bool different_cases(char a, char b) {
    return toupper(a) == toupper(b) && a != b;
}

int main() {
    stack<char> s;
    stack<int> animals;
    stack<int> traps;

    map<int, int> animal_indexes;

    int animal_count = 0,
        trap_count = 0;
    
    char cur_char;
    while(cin.get(cur_char)) {
        if (cur_char == '\n') break;

        if (isupper(cur_char)) {
            trap_count++;
            traps.push(trap_count);
        } else {
            animal_count++;
            animals.push(animal_count);
        }

        if (s.empty() || !different_cases(cur_char, s.top())) {
            s.push(cur_char);
        } else {
            animal_indexes[traps.top()] = animals.top();

            animals.pop();
            traps.pop();
            s.pop();
        }
    }

    if (!s.empty()) {
        cout << "Impossible\n";
        return 0;
    }

    cout << "Possible\n";
    for (const auto& [_trap, animal_index] : animal_indexes) {
        cout << animal_index << " ";
    }
    
    return 0;
}
