#!/usr/bin/env bash

psql -h localhost -p 9114 -U data_user -d fakebrownroad -c "
INSERT INTO table1 (name) SELECT 'До сбоя ' || g FROM generate_series(101, 103) g;
INSERT INTO table2 (value) SELECT g * 10 FROM generate_series(101, 103) g;
INSERT INTO table3 (info) SELECT 'До сбоя ' || g FROM generate_series(101, 103) g;
"
