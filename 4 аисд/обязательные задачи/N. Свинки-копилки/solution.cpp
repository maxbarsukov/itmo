#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)
#define unless(cond) if (!(cond))


void dfs(int v, bool* broken, vector<vector<int>>* graph) {
    broken[v] = true;
    for (int i : (*graph)[v]) {
        unless(broken[i]) dfs(i, broken, graph);
    }
}

int main() {
    int n, value, broken_count = 0;
    cin >> n;

    vector<vector<int>> graph(n);

    repeat(n, i) {
        cin >> value;
        graph[value - 1].push_back(i);
        graph[i].push_back(value - 1);
    }

    bool broken[n];
    fill(broken, broken + n, false);

    repeat(n, i) {
        unless (broken[i]) {
            ++broken_count;
            dfs(i, broken, &graph);
        }
    }

    cout << broken_count << endl;
    return 0;
}
