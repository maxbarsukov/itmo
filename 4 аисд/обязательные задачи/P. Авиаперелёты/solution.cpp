#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)
#define unless(cond) if (!(cond))


vector<vector<int>> graph;
vector<vector<int>> fits_fuel;

void dfs(int v, vector<bool>& visited, bool direction, int n) {
    visited[v] = true;

    repeat(n, i) {
        if (visited[i]) continue;
        if (fits_fuel[direction ? i : v][direction ? v : i]) {
            dfs(i, visited, direction, n);
        }
    }
}

bool are_all_visited(int n, bool direction) {
    vector<bool> visited(n, false);
    dfs(0, visited, direction, n);

    repeat(n, i) {
        unless(visited[i]) return false;
    }

    return true;
}

bool can_travel_with_fuel(int fuel, int n) {
    repeat(n, i) {
        repeat(n, j) {
            fits_fuel[i][j] = (graph[i][j] <= fuel);
        }
    }

    if (are_all_visited(n, false)) {
        return are_all_visited(n, true);
    }

    return false;
}

int binary_search_fuel(int max_fuel, int n) {
    int low = 0;
    int high = max_fuel;

    while (low < high) {
        int mid = (low + high) / 2;

        if (can_travel_with_fuel(mid, n)) {
            high = mid;
        } else {
            low = mid + 1;
        }
    }

    return low;
}


int main() {
    int n;
    cin >> n;

    graph.resize(n, vector<int>(n));
    fits_fuel.resize(n, vector<int>(n));

    int max_fuel_between_cities = 0;
    repeat(n, i) {
        repeat(n, j) {
            cin >> graph[i][j];
            unless (i == j) {
                max_fuel_between_cities = max(max_fuel_between_cities, graph[i][j]);
            }
        }
    }

    cout << binary_search_fuel(max_fuel_between_cities, n) << endl;
    return 0;
}
