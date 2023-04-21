-- Функция на языке PL/pgSQL для подсчета расстояние между двумя лабораториями.
-- lab1_id, lab2_id = ID лабораторий
-- unit = единица, которую вы хотите получить для результатов, где:
-- 'M' - статутные мили (по умолчанию)
-- 'K' — километры
-- 'N' — морские мили

CREATE OR REPLACE FUNCTION calculate_laboratory_distance(lab1_id int, lab2_id int, units varchar)
RETURNS float AS $dist$
    DECLARE
        dist float := 0;
        loc1_id int;
        loc2_id int;

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
        SELECT laboratories.location_id INTO loc1_id FROM laboratories WHERE laboratories.id = lab1_id;
        IF NOT FOUND THEN
            RAISE EXCEPTION 'laboratory % not found', lab1_id;
        END IF;
        SELECT laboratories.location_id INTO loc2_id FROM laboratories WHERE laboratories.id = lab2_id;
        IF NOT FOUND THEN
            RAISE EXCEPTION 'laboratory % not found', lab2_id;
        END IF;

        IF loc1_id IS NULL OR loc2_id IS NULL THEN
            RAISE EXCEPTION 'No location for laboratories';
        END IF;

        SELECT locations.coords INTO point1 FROM locations WHERE locations.id = loc1_id;
        SELECT locations.coords INTO point2 FROM locations WHERE locations.id = loc2_id;
        IF point1 IS NULL OR point2 IS NULL THEN
            RAISE EXCEPTION 'Null coords in laboratory location';
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
