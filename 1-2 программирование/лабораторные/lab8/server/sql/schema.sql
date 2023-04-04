BEGIN;

CREATE TYPE unit_of_measure AS ENUM (
    'KILOGRAMS', 'SQUARE_METERS', 'LITERS', 'MILLILITERS'
);

CREATE TYPE organization_type AS ENUM (
    'COMMERCIAL', 'GOVERNMENT', 'TRUST', 'PRIVATE_LIMITED_COMPANY'
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(40) UNIQUE NOT NULL,
    password_digest VARCHAR(64) NOT NULL,
    salt VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS organizations (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL CONSTRAINT not_empty_name CHECK(length(name) > 0),
    employees_count BIGINT NOT NULL CONSTRAINT positive_employees_count CHECK (employees_count > 0),
    type organization_type NOT NULL,
    street VARCHAR NOT NULL CONSTRAINT not_empty_street CHECK(length(street) > 0),
    zip_code VARCHAR CONSTRAINT zip_code_ge_6 CHECK (zip_code IS NULL OR length(zip_code) >= 6),
    creator_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL CONSTRAINT not_empty_name CHECK(length(name) > 0),
    x INTEGER NOT NULL,
    y BIGINT NOT NULL,
    creation_date DATE DEFAULT NOW() NOT NULL,
    price BIGINT NOT NULL CONSTRAINT positive_price CHECK (price > 0),
    part_number VARCHAR CONSTRAINT not_empty_part_number CHECK(part_number IS NULL OR length(part_number) > 0),
    unit_of_measure unit_of_measure,
    manufacturer_id INT REFERENCES organizations(id) ON DELETE SET NULL,
    creator_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

END;
