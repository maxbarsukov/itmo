BEGIN;

CREATE TYPE sex AS ENUM ('М', 'Ж');
CREATE TYPE actions AS ENUM (
    'войти в лабораторию',
    'уйти обедать',
    'расстегнуть молнию',
    'достать баночку',
    'отвинить дно',
    'заглянуть внутрь'
);

CREATE TYPE brands AS ENUM (
    'Жиллетт'
);

CREATE DOMAIN uint2 AS int4 CHECK(VALUE >= 0 AND VALUE < 65536);

CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    current_location VARCHAR(200) NOT NULL DEFAULT 'кое-где',
    sex sex NOT NULL
);

CREATE TABLE items (
    id SERIAL PRIMARY KEY
);

CREATE TABLE people_actions (
    id SERIAL PRIMARY KEY,
    action actions NOT NULL,
    item_id INT REFERENCES items(id),
    person_id INT REFERENCES people(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE backpacks (
    id SERIAL PRIMARY KEY,
    is_zipper_fastened BOOLEAN NOT NULL DEFAULT TRUE,
    person_id INT REFERENCES people(id),
    item_id INT REFERENCES items(id)
);

CREATE TABLE shaving_foams (
    id SERIAL PRIMARY KEY,
    brand brands NOT NULL,
    is_bottom_in_place BOOLEAN NOT NULL DEFAULT TRUE,
    backpack_id INT REFERENCES backpacks(id),
    item_id INT REFERENCES items(id)
);

CREATE TABLE cylinders (
    id SERIAL PRIMARY KEY,
    volume uint2 NOT NULL,
    is_big BOOLEAN GENERATED ALWAYS AS (volume > 100) STORED,
    shaving_foam_id INT NOT NULL REFERENCES shaving_foams(id),
    item_id INT REFERENCES items(id)
);

CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL REFERENCES people(id)
);

CREATE TABLE breaks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    from_time TIME NOT NULL,
    to_time TIME NOT NULL,
    employee_id INT NOT NULL REFERENCES employees(id)
);

CREATE TABLE research_areas (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE laboratories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL
);

CREATE TABLE appointments (
    laboratory_id INT NOT NULL REFERENCES laboratories(id),
    employee_id INT NOT NULL REFERENCES employees(id),
    CONSTRAINT appointment_id PRIMARY KEY (laboratory_id, employee_id)
);

CREATE TABLE development_directions (
    laboratory_id INT NOT NULL REFERENCES laboratories(id),
    research_area_id INT NOT NULL REFERENCES research_areas(id),
    CONSTRAINT development_direction_id PRIMARY KEY (laboratory_id, research_area_id)
);

INSERT INTO people(name, sex, current_location)
VALUES ('Недри', 'М', 'у входа в лабораторию'),
       ('Аноним', 'М', 'столовая'),
       ('Аноним', 'Ж', 'столовая');

--- доделать

INSERT INTO items VALUES (default);
INSERT INTO backpacks(person_id, item_id) VALUES (1, 1);

INSERT INTO items VALUES (default);
INSERT INTO shaving_foams(brand, is_bottom_in_place, backpack_id, item_id) VALUES ('Жиллетт', TRUE, 1, 2);

INSERT INTO items VALUES (default);
INSERT INTO cylinders(volume, shaving_foam_id, item_id) VALUES (100, 1, 3);

INSERT INTO employees(person_id) VALUES (2), (3);

INSERT INTO breaks(name, from_time, to_time, employee_id)
VALUES ('Обед', '15:00', '15:30', 1),
       ('Перекур', '15:00', '17:45', 2);

INSERT INTO research_areas(name) VALUES ('Оплодотворение');
INSERT INTO laboratories(name) VALUES ('ауд. 365');

INSERT INTO appointments(laboratory_id, employee_id) VALUES (1, 1), (1, 2);
INSERT INTO development_directions(laboratory_id, research_area_id) VALUES (1, 1);

COMMIT;
