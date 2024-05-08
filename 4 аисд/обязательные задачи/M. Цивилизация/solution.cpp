#include <bits/stdc++.h>

using namespace std;

#define repeat(times, i) for (int (i) = 0; (i) < (times); (i)++)
#define unless(cond) if (!(cond))

const int INF = numeric_limits<int>::max();


int main() {
    int n, m;
    cin >> n >> m;

    pair<int, int> src, dest; // (x, y)
    cin >> src.first >> src.second >> dest.first >> dest.second;
    --src.first; --src.second; --dest.first; --dest.second;

    if (src.first == dest.first && src.second == dest.second) {
        cout << 0 << endl;
        return 0;
    }

    vector<string> map(n);
    repeat(n, i) cin >> map[i];

    vector<vector<int>> cost(n, vector<int>(m, INF));
    vector<vector<pair<int, int>>> parent(n, vector<pair<int, int>>(m, {-1, -1}));

    auto comp = [&](const pair<int, int>& a, const pair<int, int>& b) {
        return cost[a.first][a.second] > cost[b.first][b.second];
    };

    priority_queue<pair<int, int>, vector<pair<int, int>>, decltype(comp)> pq(comp);
    cost[src.first][src.second] = 0;


    pq.push(src);

    int dx[] = {1, -1, 0, 0};
    int dy[] = {0, 0, 1, -1};
    char dir[] = {'N', 'S', 'W', 'E'};

    while (!pq.empty()) {
        auto [x, y] = pq.top();
        pq.pop();

        if (x == dest.first && y == dest.second) break;

        repeat(4, d) {
            int nx = x + dx[d], ny = y + dy[d];
            if (nx >= 0 && nx < n && ny >= 0 && ny < m && map[nx][ny] != '#') {
                int new_cost = cost[x][y] + (map[nx][ny] == '.' ? 1 : (map[nx][ny] == 'W' ? 2 : 0));
                if (new_cost < cost[nx][ny]) {
                    cost[nx][ny] = new_cost;
                    parent[nx][ny] = {x, y};
                    pq.push({nx, ny});
                }
            }
        }
    }


    if (cost[dest.first][dest.second] == INF) {
        cout << "-1" << endl;
        return 0;
    }

    cout << cost[dest.first][dest.second] << endl;

    string path;
    int cx = dest.first, cy = dest.second;
    while (cx != src.first || cy != src.second) {
        auto [px, py] = parent[cx][cy];
        repeat(4, d) {
            if (px == cx + dx[d] && py == cy + dy[d]) {
                path.push_back(dir[d]);
                break;
            }
        }
        cx = px;
        cy = py;
    }

    reverse(path.begin(), path.end());
    cout << path << endl;

    return 0;
}
