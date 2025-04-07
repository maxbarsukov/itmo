with ts_info as (
    select oid, spcname
    from pg_tablespace
),
db_ts as (
    select ts.spcname as ts_name,
        db.datname as obj_name,
        'database' as obj_type,
        NULL as schema_name
    from pg_database db
    join ts_info ts on db.dattablespace = ts.oid
),
rel_obj as (
    select
        coalesce(t.spcname, 'pg_default') AS ts_name,
        c.relname as obj_name,
        case c.relkind
            when 'r' then 'table'
            when 'i' then 'index'
            when 'S' then 'sequence'
            when 'v' then 'view'
            when 'm' then 'materialized view'
            when 'c' then 'composite type'
            when 't' then 'TOAST table'
            when 'f' then 'foreign table'
            when 'p' then 'partitioned table'
            when 'I' then 'partitioned index'
            else c.relkind::text
        end as obj_type,
        n.nspname as schema_name
    from pg_class c
    join pg_namespace n on c.relnamespace = n.oid
    left join ts_info t on c.reltablespace = t.oid
    where c.relkind in ('r', 'i', 'S', 'v', 'm', 'c', 't', 'f', 'p', 'I')
),
empty_ts as (
    select
        ts.spcname AS ts_name,
        '<empty>' AS obj_name,
        NULL AS obj_type,
        NULL AS schema_name
    from ts_info ts
    where not exists (
        select 1 from db_ts where ts_name = ts.spcname
    )
    and not exists (
        select 1 from rel_obj where ts_name = ts.spcname
    )
)
select
    case
        when row_number() over (partition by ts_name order by obj_type, obj_name) = 1
        then ts_name
        else NULL
    end as "tablespace",
    case
        when schema_name IS NOT NULL AND obj_name IS NOT NULL AND obj_name != '<empty>'
            then schema_name || '.' || obj_name
        else obj_name
    end as "object",
    obj_type as "type"
from (
    select * from db_ts
    union all
    select * from rel_obj
    union all
    select * from empty_ts
) all_obj
order by ts_name, obj_type, schema_name, obj_name;
