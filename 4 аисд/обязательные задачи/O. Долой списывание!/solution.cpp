#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)
#define unless(cond) if (!(cond))


bool bipartite_dfs(vector<vector<int>>& graph, int node, vector<int>& color) {
    for (auto it : graph[node]) {
        if (color[it] == -1) {
            color[it] = 1 - color[node];

            unless (bipartite_dfs(graph, it, color)) return false;
        } else if(color[it] == color[node]) {
            return false;
        }
    }

    return true;
}

bool is_bipartite(vector<vector<int>>& graph, int n) {
    vector<int> color(n, -1);

    repeat(n, i) {
        if (color[i] == -1) {
            color[i] = 1;
            unless (bipartite_dfs(graph, i, color)) return false;
        }
    }

    return true;
}


int main() {
    int n, m;
    cin >> n >> m;

    vector<vector<int>> graph(n);

    int pupil1, pupil2;
    repeat(m, i) {
        cin >> pupil1 >> pupil2;
        graph[pupil1 - 1].push_back(pupil2 - 1);
        graph[pupil2 - 1].push_back(pupil1 - 1);
    }

    cout << (is_bipartite(graph, n) ? "YES" : "NO") << endl;
    return 0;
}
