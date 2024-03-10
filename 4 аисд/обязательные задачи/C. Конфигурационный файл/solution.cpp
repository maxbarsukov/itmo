#include <bits/stdc++.h>

using namespace std;

pair<string, string> parse_assignment(string s) {
    size_t pos = s.find('=');
    pair<string, string> result(s.substr(0, pos), s.substr(pos + 1, s.size() - pos));
    return result;
}

inline bool is_number(const string& s) {
    return isdigit(s[0]) || s[0] == '-';
}

int get_val_by_var(map<string, stack<int>> &config, const string& variable) {
    if (!config.count(variable) || config[variable].empty()) {
        return 0;
    }
    return config[variable].top();
}

inline void clean_current_level(map<string, stack<int>> &config, stack<queue<string>> &context_vars) {
    while (!context_vars.top().empty()) {
        config[context_vars.top().front()].pop();
        context_vars.top().pop();
    }
    context_vars.pop();
}

int main() {
    cin.tie(0);
    cout.tie(0);

    map<string, stack<int>> config;
    stack<queue<string>> context_vars;
    
    context_vars.emplace();
    string current_line;

    while (getline(cin, current_line)) {
        if (current_line == "{") {
            context_vars.emplace();
        } else if (current_line == "}") {
            clean_current_level(config, context_vars);
        } else {
            const auto& [variable, value] = parse_assignment(current_line);

            int assigned_value;
            if (is_number(value)) {
                assigned_value = stoi(value);
            } else {
                assigned_value = get_val_by_var(config, value);
                cout << assigned_value << endl;
            }

            context_vars.top().push(variable);
            config[variable].push(assigned_value);
        }
    }

    return 0;
}
