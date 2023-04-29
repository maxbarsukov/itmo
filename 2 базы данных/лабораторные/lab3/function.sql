-- Функция на языке PL/pgSQL для подсчета расстояние между двумя лабораториями.
-- lab1_id, lab2_id = ID лабораторий
-- unit = единица, которую вы хотите получить для результатов, где:
-- 'M' - статутные мили (по умолчанию)
-- 'K' — километры
-- 'N' — морские мили

CREATE OR REPLACE FUNCTION calculate_distance(loc1_id int, loc2_id int, units varchar)
RETURNS float AS $dist$
    DECLARE
        dist float := 0;
        point1 point;
        point2 point;

        -- lat1, lon1 = широта и долгота точки 1 (в десятичных градусах)
        -- lat2, lon2 = широта и долгота точки 2 (в десятичных градусах)
        lat1 int;
        lat2 int;
        lon1 int;
        lon2 int;

        radlat1 float;
        radlat2 float;
        theta float;
        radtheta float;
    BEGIN
        IF loc1_id IS NULL OR loc2_id IS NULL THEN
            RAISE EXCEPTION 'Location is null!';
        END IF;

        SELECT locations.coords INTO point1 FROM locations WHERE locations.id = loc1_id;
        IF NOT FOUND THEN
            RAISE EXCEPTION 'location % not found', lab1_id;
        END IF;
        SELECT locations.coords INTO point2 FROM locations WHERE locations.id = loc2_id;
        IF NOT FOUND THEN
            RAISE EXCEPTION 'location % not found', lab1_id;
        END IF;

        IF point1 IS NULL OR point2 IS NULL THEN
            RAISE EXCEPTION 'Null coords in location';
        END IF;

        lat1 := point1[0];
        lon1 := point1[1];
        lat2 := point2[0];
        lon2 := point2[1];

        IF lat1 = lat2 AND lon1 = lon2
            THEN RETURN dist;
        ELSE
            radlat1 := pi() * lat1 / 180;
            radlat2 := pi() * lat2 / 180;
            theta := lon1 - lon2;
            radtheta := pi() * theta / 180;
            dist := sin(radlat1) * sin(radlat2) + cos(radlat1) * cos(radlat2) * cos(radtheta);

            IF dist > 1 THEN dist = 1; END IF;

            dist := acos(dist);
            dist := dist * 180 / pi();
            dist := dist * 60 * 1.1515;

            IF units = 'K' THEN dist := dist * 1.609344; END IF;
            IF units = 'N' THEN dist := dist * 0.8684; END IF;

            RETURN dist;
        END IF;
    END;
$dist$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_distance_to_laboratory() RETURNS trigger AS $update_distance_to_laboratory$
    DECLARE
        person_id int;
        cur_loc_id int;
        lab_loc_id int;
    BEGIN
        NEW.distance_to_laboratory := NULL;

        IF NEW.laboratory_id IS NULL THEN
            RAISE EXCEPTION 'laboratory_id cannot be null';
        END IF;
        IF NEW.employee_id IS NULL THEN
            RAISE EXCEPTION 'employee_id cannot be null';
        END IF;

        SELECT employees.person_id INTO person_id FROM employees WHERE employees.id = NEW.employee_id;
        SELECT people.current_location_id INTO cur_loc_id FROM people WHERE people.id = person_id;
        SELECT laboratories.location_id INTO lab_loc_id FROM laboratories WHERE laboratories.id = NEW.laboratory_id;

        NEW.distance_to_laboratory := calculate_distance(cur_loc_id, lab_loc_id, 'K');
        RETURN NEW;
    END;
$update_distance_to_laboratory$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER update_distance_to_laboratory BEFORE INSERT OR UPDATE ON appointments
    FOR EACH ROW
    EXECUTE FUNCTION update_distance_to_laboratory();
