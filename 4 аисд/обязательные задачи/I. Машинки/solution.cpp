#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)
#define NOT_SET_YET -1

int get_car_id(int* cars, int step) {
    return cars[step] - 1;
}

int main() {
    int n, k, p;
    cin >> n >> k >> p;
    
    int played_cars[p];
    repeat(p, i) cin >> played_cars[i];

    vector<int> last_step_for_car(n, NOT_SET_YET);

    // для каждого шага информация о следующем шаге, в котором будет использована машинка с этого шага
    vector<int> next_step_to_use_car(p, INT_MAX);

    repeat(p, i) {
        int car_id = get_car_id(played_cars, i);
        int current_last_step_for_i = last_step_for_car[car_id];

        if (current_last_step_for_i != NOT_SET_YET) {
            next_step_to_use_car[current_last_step_for_i] = i;
        }

        last_step_for_car[car_id] = i;
    }

    int operations_count = 0;
    set<int> current_steps_for_cars_on_floor;

    repeat(p, i) {
        if (current_steps_for_cars_on_floor.count(i)) {
            current_steps_for_cars_on_floor.erase(i);
        } else {
            operations_count++;

            if (current_steps_for_cars_on_floor.size() == k) {
                current_steps_for_cars_on_floor.erase(--current_steps_for_cars_on_floor.end());
            }
        }

        current_steps_for_cars_on_floor.insert(next_step_to_use_car[i]);
    }

    cout << operations_count << endl;
    return 0;
}
