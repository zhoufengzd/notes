# some google big query notes

## analytic functions:
* lead / lag, percentile

## regex
* `regex_contains`
* `regex_replace(data, r'pattern1|pattern2|pattern3', '')`

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
    select table_id, sum(size_bytes)/(1024*1024) as size_MB
        from `${dataset_name}.__TABLES__`
            group by table_id
)
select * from dt order by size_MB desc;
```