DO $$
DECLARE
    rec RECORD;
BEGIN
    RAISE NOTICE '№   Имя ограничения           Тип Текст ограничения';
    RAISE NOTICE '-------------------------- -- -------------------------------------------------';
    FOR rec IN
        SELECT
            ROW_NUMBER() OVER (ORDER BY c.conname) AS sequence_number,
            c.conname AS constraint_name,
            CASE
                WHEN c.contype = 'c' THEN 'CHECK'
                ELSE 'NOT NULL'
            END AS constraint_type,
            t.relname AS table_name,
            a.attname AS column_name,
            pg_get_constraintdef(c.oid) AS constraint_text
        FROM
            pg_constraint c
        JOIN
            pg_catalog.pg_namespace nsp ON nsp.oid = connamespace
        JOIN
            pg_class t ON c.conrelid = t.oid
        JOIN
            pg_attribute a ON a.attnum = ANY(c.conkey) AND a.attrelid = t.oid
        WHERE
            (c.contype = 'c' OR (a.attnotnull))
            AND t.relkind = 'r'
        ORDER BY
            sequence_number
    LOOP
        RAISE NOTICE '% % % %',
            rec.sequence_number, rec.constraint_name, rec.constraint_type, rec.constraint_text;
    END LOOP;
END $$;
