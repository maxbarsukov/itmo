#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)

struct halved_list {
    size_t size = 0;
    deque<int> first_half;
    deque<int> last_half;
};

struct goblin {
    int id;
    bool is_privileged;
};


void move_between_halves(struct halved_list* goblins) {
    if (!goblins->last_half.empty()) {
        goblins->first_half.push_back(goblins->last_half.front());
        goblins->last_half.pop_front();
    }
}

void equalize_halves(struct halved_list* goblins) {
    if (goblins->size % 2 == 0) move_between_halves(goblins);
}

void push_goblin(struct halved_list* goblins, int new_goblin) {
    goblins->last_half.push_back(new_goblin);
    
    equalize_halves(goblins);
}

void push_privileged_goblin(struct halved_list* goblins, int new_goblin) {
    if (goblins->size % 2 == 0) {
        goblins->first_half.push_back(new_goblin);
    } else {
        goblins->last_half.push_front(new_goblin);
    }
}


int pop(struct halved_list* goblins) {
    int value = goblins->first_half.front();
    goblins->first_half.pop_front();

    equalize_halves(goblins);

    goblins->size--;
    return value;
}

void push(struct halved_list* goblins, goblin new_goblin) {
    if (new_goblin.is_privileged) {
        push_privileged_goblin(goblins, new_goblin.id);
    } else {
        push_goblin(goblins, new_goblin.id);
    }
    goblins->size++;
}   


int main() {
    int n, new_goblin;
    cin >> n;

    halved_list goblins;

    char command;
    repeat(n, i) {
        cin >> command;

        if (command == '-') {
            cout << pop(&goblins) << endl;
        } else {
            cin >> new_goblin;
            push(&goblins, { .id = new_goblin, .is_privileged = (command == '*') });
        }
    }

    return 0;
}
