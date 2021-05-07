# Postgres Notes

## setup / initial connection
* config: /usr/share/postgresql
* connect: `sudo -u postgres psql`
* run sql file: `\i <external.sql file>`

### System command
* Switch database: `\connect <db>`
* exit: '\q'
* execute sheel command: `\! <cmd>`
* clear screen:
```
clear screen
\! clear
```

## db info
* settings: `select * from pg_settings;`
* config
```
SHOW ALL;
SHOW max_connections;
SHOW config_file;
```
* list everything
```
\l  # List all databases
\d  # list all db objects (table, view, etc)
\du # List all users/roles
\dv # List all views
\dt # Show all tables in database
\h  # List help information
\d <table name>
```


## Create db, schema, user
* db -> user
```
DROP DATABASE test;
CREATE DATABASE test;
CREATE USER tomato WITH PASSWORD 'tomato';
ALTER USER tomato WITH PASSWORD 'tomato';
ALTER USER tomato WITH SUPERUSER;
ALTER DATABASE test RENAME TO test;
ALTER DATABASE test OWNER TO tomato;
```
* schema
```
CREATE SCHEMA name;
```

## backup db
* dump database and restore to a new database
```
pg_dump -Fc test > test.dump
pg_restore -d newtest test.dump
```


## checking tables / views
* query table info
```
SELECT table_schema, table_name
    FROM information_schema.tables
        WHERE table_schema not in ('information_schema', 'pg_catalog')
    ORDER BY table_schema,table_name;
```

* check ddl
```
select definition from pg_views where viewname = 'my_view';
select * from pg_tables where tablename = 'depth_coverage';
```

* list row count
```
SELECT schemaname, relname as tablename, n_live_tup as rowcount
  FROM pg_stat_user_tables
    ORDER BY n_live_tup DESC;

SELECT reltuples::bigint AS row_count_estimate
    FROM pg_class where relname='depth_coverage';
```
* list object size
```
select pg_size_pretty(pg_database_size('urlcov'));
select relname as objectname, relkind as objecttype, pg_size_pretty(pg_table_size(oid)) as size
    from pg_class
        where relkind in ('r', 'i', 'm') -- ordinary table, index, materialized view
    order by pg_table_size(oid) desc;
```

* check table settings:
```
select relname, reloptions, pg_namespace.nspname
    from pg_class
        join pg_namespace on pg_namespace.oid = pg_class.relnamespace
    where relname like 'customer%' and pg_namespace.nspname = 'public';
```
* list indexes
```
SELECT tablename, indexname
    FROM pg_indexes
        WHERE schemaname = 'public'
            and tablename = 'depth_coverage'
    ORDER BY tablename, indexname;
```
* list partitions
```
select * from pg_inherits;

select relname as table_name
    from pg_class c
        join pg_index i on i.indrelid = c.oid
    where relkind = 'r' and relhasindex and i.indisclustered
```

## export / import table
```
\COPY (select * from product) to 'product.csv' WITH CSV HEADER QUOTE '"' DELIMITER ',';

export POSTGRES_HOST=localhost
export POSTGRES_DB=people
export POSTGRES_USER=$USER
export PGPASSWORD=$USER
cat employee_count.csv | psql -v ON_ERROR_STOP=1 -w \
    -h $POSTGRES_HOST -d $POSTGRES_DB -U $POSTGRES_USER \
    -c 'COPY employee_count(company_id, work_month, employee_counts) FROM STDIN CSV HEADER'
```


## activity and connection
* list active connections:
```
select datname, pid, query_start, trim(substring(query, 1, 512)) as query
    from pg_stat_activity
        where length(query) > 0
            and state <> 'idle'
        and query not like 'select datname, pid, query_start%';

select count(1) from pg_stat_activity;
select count(1) from pg_stat_activity where state = 'idle';
```

* terminate idle connections
```
select pg_terminate_backend(pid)
    from pg_stat_activity
        where datname = 'urlcov'
	       and pid <> pg_backend_pid()
	       and state in ('idle', 'idle in transaction', 'idle in transaction (aborted)', 'disabled')
           and state_change < current_timestamp - interval '15' minute;
```    

## ltree & hstore
### setup:
```
CREATE EXTENSION ltree;
CREATE EXTENSION hstore;
```

### notes
* ltree
    * faster than using a recursive CTE or recursive function
    * Has built in query syntax and operators
    * Constraints: tree structure needs to be maintained
* hstore
    * multiple key-value pairs in a single value.
    * query: `SELECT * FROM each('aaa=>bq, b=>NULL, ""=>1');`


## full text search
* fuzzy search with 3+ characters: pg_trgm
* reference: https://alibaba-cloud.medium.com/postgresql-fuzzy-search-best-practices-single-word-double-word-and-multi-word-fuzzy-search-b375c6d0cf6e

## Tuning
http://engineroom.trackmaven.com/blog/so-you-want-another-postgresql-database-part-1/
http://engineroom.trackmaven.com/blog/so-you-want-another-postgresql-database-part-2/
http://engineroom.trackmaven.com/blog/so-you-want-another-postgresql-database-part-3/
https://devcenter.heroku.com/articles/postgresql-indexes
citusdb: https://blog.cloudflare.com/scaling-out-postgresql-for-cloudflare-analytics-using-citusdb/
