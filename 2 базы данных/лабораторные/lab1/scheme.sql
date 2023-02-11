BEGIN;

CREATE TYPE sex AS ENUM ('М', 'Ж');
CREATE TYPE employment_type AS ENUM ('Полная', 'Частичная', 'Вахта');

CREATE DOMAIN uint2 AS int4 CHECK(VALUE >= 0 AND VALUE < 65536);

CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    sex sex NOT NULL
);

CREATE TABLE backpacks (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL REFERENCES people(id)
);

CREATE TABLE shaving_foams (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(30),
    is_bottom_in_place BOOLEAN NOT NULL DEFAULT TRUE,
    backpack_id INT NOT NULL REFERENCES backpacks(id)
);

CREATE TABLE cylinders (
    id SERIAL PRIMARY KEY,
    volume uint2 NOT NULL,
    is_big BOOLEAN GENERATED ALWAYS AS (volume > 100) STORED,
    shaving_foam_id INT NOT NULL REFERENCES shaving_foams(id)
);

CREATE TABLE schedules (
    id SERIAL PRIMARY KEY,
    type employment_type
);

CREATE TABLE breaks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    from_time TIME NOT NULL,
    to_time TIME NOT NULL
);

CREATE TABLE daily_routines (
    schedule_id INT NOT NULL REFERENCES schedules(id),
    break_id INT NOT NULL REFERENCES breaks(id),
    CONSTRAINT daily_routineid PRIMARY KEY (schedule_id, break_id)
);

CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL REFERENCES people(id),
    schedule_id INT NOT NULL REFERENCES schedules(id)
);

CREATE TABLE research_areas (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE laboratories (
    id SERIAL PRIMARY KEY,
    auditorium VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
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

INSERT INTO people(name, sex)
VALUES ('Недри', 'М'),
       ('Аноним', 'М'),
       ('Аноним', 'Ж');

INSERT INTO backpacks(person_id) VALUES (1);
INSERT INTO shaving_foams(brand, is_bottom_in_place, backpack_id) VALUES ('Жиллетт', FALSE, 1);
INSERT INTO cylinders(volume, shaving_foam_id) VALUES (100, 1);

INSERT INTO schedules(type) VALUES ('Полная');
INSERT INTO breaks(name, from_time, to_time)
VALUES ('Обед', '15:00', '15:30'),
       ('Перекур', '17:30', '17:45');

INSERT INTO daily_routines(schedule_id, break_id) VALUES (1, 1), (1, 2);

INSERT INTO employees(person_id, schedule_id) VALUES (2, 1), (3, 1);

INSERT INTO research_areas(name) VALUES ('Оплодотворение');
INSERT INTO laboratories(auditorium) VALUES ('ауд. 1328 (бывш. 371)');

INSERT INTO appointments(laboratory_id, employee_id) VALUES (1, 1), (1, 2);
INSERT INTO development_directions(laboratory_id, research_area_id) VALUES (1, 1);

COMMIT;
