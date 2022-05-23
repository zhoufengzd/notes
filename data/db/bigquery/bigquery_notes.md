# some google big query notes

## analytic functions:
* lead / lag, percentile

## regex
* `regex_contains`
* `regex_replace(data, r'pattern1|pattern2|pattern3', '')`

## hex / bytes
```
with dt as (
    select 'hello' as msg
    union all select 'world!'
)
select msg, cast(msg as bytes) as b_msg,
        to_hex(cast(msg as bytes)) as hex_msg,
        cast(from_hex(to_hex(cast(msg as bytes))) as string) as from_hex_msg
    from dt;
```

## datetime, timestamp
```
parse_timestamp('%Y%m%dT%H%M%S', '20210703T042504')
```

## external table
*
```
create or replace table ... (
    ...
)
with partition columns (
    logged_day date
)
options (
    uri='gs://...',
    format='JSON', compression='GZIP', hive_partition_uri_prefix='gs://.../{base_table}',
    require_hive_partition_filter=true
);


export data
    options(uri='gs://...', format='JSON', compression='GZIP', overwrite=true)
as
    select *
        from ...
```

## table size:
```
with dt as (
    select dataset_id as db_name, table_id as table_name,
            round(size_bytes/(1024*1024), 3) as size_mb, row_count as table_rows,
            last_modified_time as update_time, current_timestamp() as logged_time
        from `${dataset_name}.__TABLES__`
)
select * from dt order by size_mb desc;
```

## dynamic sql
* performance: ok
```
begin
    declare create_sql string;
    declare row_id int64 default 0;
    declare row_id_max int64 default 0;

    execute immediate 'select max(rn) from ...' into row_id_max;
    loop
        set row_id = row_id + 1;
        if row_id > row_id_max then
            break;
        end if;

        execute immediate 'select create_sql from ... where rn = ?' into create_sql using row_id;
        execute immediate create_sql;
    end loop;
end;
```

## connection
```
bq ls --connection --project_id=clearlabs-quality --location=us
bq rm --connection --project_id=clearlabs-quality --location=us clearview

## TODO: fill username, password for mysql
bq mk --connection --display_name='clearview' \
    --connection_type='CLOUD_SQL' \
    --properties='{"instanceId":"clearlabs-quality:us-west2:clearview-replica","database":"information_schema","type":"MYSQL"}'  \
    --connection_credential='{"username":"", "password":""}' \
    --project_id=clearlabs-quality --location="us" \
    clearview
```