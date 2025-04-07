INSERT INTO table1 (name) SELECT 'Имя ' || g FROM generate_series(1, 100) g;
INSERT INTO table2 (value) SELECT g * 10 FROM generate_series(1, 100) g;
INSERT INTO table3 (info) SELECT 'Инфо ' || g FROM generate_series(1, 100) g;
