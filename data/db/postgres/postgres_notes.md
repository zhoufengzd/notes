# Postgres Notes

## Installations
* config: /usr/share/postgresql
* data directory: data_directory

## Initial Setup
### Connect
```
psql -d postgres
sudo -u postgres psql
```
### Reset postgres db / dump db    
* reset postgres db
`drop database postgres; create database postgres; \c postgres;`

### dump db and restore to newdb
```
pg_dump -Fc test > test.dump
pg_restore -d newdb test.dump
pg_dump -s <databasename>
```

## System command
[comment]: # (Switch database)
    \connect DBNAME
[comment]: # (exit)
    \q
[comment]: # (get shell and execute command)
    \! <cmd>
[comment]: # (system command)
    clear screen
    \! clear

## db info
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

* settings
```
select * from pg_settings;
```

* config
```
SHOW ALL;
SHOW max_connections;
SHOW config_file;
```

### DDL and DML
    \i <external.sql file>

#### Create db, schema, user
```
DROP DATABASE test;
CREATE DATABASE test;
CREATE DATABASE tomato;
CREATE SCHEMA name;
CREATE USER tomato WITH PASSWORD 'tomato';
ALTER USER tomato WITH PASSWORD 'tomato';
ALTER USER tomato WITH SUPERUSER;
ALTER DATABASE test RENAME TO test;
ALTER DATABASE test OWNER TO tomato;
```

[comment]: # (create extension)
    CREATE EXTENSION ltree; CREATE EXTENSION hstore;

#### export view ddl
    select definition from pg_views where viewname = 'my_view';
    select * from pg_tables where tablename = 'depth_coverage';

#### list tables
    \dt
```
SELECT table_schema, table_name
    FROM information_schema.tables
        WHERE table_schema not in ('information_schema', 'pg_catalog')
    ORDER BY table_schema,table_name;
```

#### export table
    \COPY (select * from attack_surface) to 'attack_surface.csv' WITH CSV HEADER QUOTE '"' DELIMITER ',';

#### list row count
```
SELECT schemaname, relname as tablename, n_live_tup as rowcount
  FROM pg_stat_user_tables
    ORDER BY n_live_tup DESC;

SELECT reltuples::bigint AS row_count_estimate
    FROM pg_class where relname='depth_coverage';
```

### check table settings:
```
select relname, reloptions, pg_namespace.nspname
    from pg_class
        join pg_namespace on pg_namespace.oid = pg_class.relnamespace
    where relname like 'depth%' and pg_namespace.nspname = 'public';
```

### list object size
```
select pg_size_pretty(pg_database_size('urlcov'));
select relname as objectname, relkind as objecttype, pg_size_pretty(pg_table_size(oid)) as size
    from pg_class
        where relkind in ('r', 'i', 'm') -- ordinary table, index, materialized view
    order by pg_table_size(oid) desc;
```

### postgres special: HSTORE (key value)
    SELECT * FROM each('aaa=>bq, b=>NULL, ""=>1');  
    # :: convert function

#### list indexes
```
SELECT tablename, indexname
    FROM pg_indexes
        WHERE schemaname = 'public'
            and tablename = 'depth_coverage'
    ORDER BY tablename, indexname;
```

#### list partitions
```
select * from pg_inherits;

select relname as table_name
    from pg_class c
        join pg_index i on i.indrelid = c.oid
    where relkind = 'r' and relhasindex and i.indisclustered
```

#### list active connections:
```
select datname, pid, query_start, trim(substring(query, 1, 512)) as query
    from pg_stat_activity
        where length(query) > 0
            and state <> 'idle'
        and query not like 'select datname, pid, query_start%';
```

select count(1) from pg_stat_activity;
select count(1) from pg_stat_activity where state = 'idle';

[comment]: # (terminate idle connections)
```
select pg_terminate_backend(pid)
    from pg_stat_activity
        where datname = 'urlcov'
	       and pid <> pg_backend_pid()
	       and state in ('idle', 'idle in transaction', 'idle in transaction (aborted)', 'disabled')
           and state_change < current_timestamp - interval '15' minute;
```    

## ltree & hstore
```
CREATE EXTENSION ltree;
CREATE EXTENSION hstore;
```

### Why use ltree?
* It will be generally faster than using a recursive CTE or recursive function
* Has built in query syntax and operators
* Constraints: tree structure needs to be maintained

### hstore
* multiple key-value pairs in a single value.


## Tuning
http://engineroom.trackmaven.com/blog/so-you-want-another-postgresql-database-part-1/
http://engineroom.trackmaven.com/blog/so-you-want-another-postgresql-database-part-2/
http://engineroom.trackmaven.com/blog/so-you-want-another-postgresql-database-part-3/
https://devcenter.heroku.com/articles/postgresql-indexes
citusdb: https://blog.cloudflare.com/scaling-out-postgresql-for-cloudflare-analytics-using-citusdb/
