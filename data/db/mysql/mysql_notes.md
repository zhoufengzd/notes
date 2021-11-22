# MySql Notes

## mysql services
```
/etc/init.d/mysql start
/etc/init.d/mysql stop
/etc/init.d/mysql restart
```

## connection
```
mysql -u root -p
MYSQL_PWD=airflow mysql -h 127.0.0.1 --port 3306 -u airflow
```
* list active connections:
```
select id, user, host, db, command, time, state, info
    from information_schema.processlist;

select db, user, count(1) as connection_count
    from information_schema.processlist
        group by db, user
    order by db, user;
```

## user and security
```
grant select on *.* to '${db_user}'@'%' identified by '${db_user_pwd}';
grant all privileges on *.* to airflow identified by '${db_user_pwd}';

-- change own password
set password='blabla';

create user ${db_user} identified by '${db_user_pwd}';
grant all privileges on *.* to '${db_user}';

select user(); -- check current logon user
select host, user from mysql.user;  -- show all users
```

## database
* create and use database
```
show databases;

create database ${db_name};
drop database ${db_name};
use ${db_name};
```

* backup and restore
```
MYSQL_PWD=${db_user_pwd} mysql -h 127.0.0.1 -u ${db_user} ${db_name} > {db_name}.sql
MYSQL_PWD=${db_user_pwd} mysql -h 127.0.0.1 -u ${db_user} ${db_name} < ${db_name}.sql
```

## table
* check db / table info
```
show tables;
show table status;
show index from table_name;
show create table table_name;
show procedure status where db = 'airflow';

show processlist;
```

* db and table size
```
select table_schema as db_name,
        round(sum(data_length + index_length) / 1048576, 1) size_mb
    from information_schema.tables
        where (1=1)
            and table_schema not in ('information_schema', 'mysql', 'performance_schema', 'sys')
        group by table_schema;

select table_schema as db_name, table_name, round((data_length + index_length) / 1048576, 3) size_mb, table_rows, update_time
    from information_schema.tables
        where table_schema not in ('information_schema', 'mysql', 'performance_schema', 'sys')
    order by table_schema, table_name;
```

* table ddl
```
create table example (...);
drop table tablename;
```
* table columns
```
select table_schema as db_name, table_name, column_name, data_type
    from information_schema.columns
        where table_schema not in ('information_schema', 'mysql', 'performance_schema', 'sys')
        order by table_schema, table_name, column_name;
```

## run query from file
```
MYSQL_PWD=${db_user_pwd} mysql -h 127.0.0.1 -u ${db_user}  < ${sql_file}
```

## export to csv
```
* in mysql console
SELECT 1 as id
    into outfile 'out.csv'
        fields terminated by ',' enclosed by '"' lines terminated by '\n';

* commandline
MYSQL_PWD=${db_user_pwd} mysql -h 127.0.0.1 -u ${db_user} -e "select 1 as id;" > out.tsv
```