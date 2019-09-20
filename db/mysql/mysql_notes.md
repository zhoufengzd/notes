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
mysql -h 127.0.0.1 --port 3306 -u airflow -p airflow
```

## security
```
alter user 'root'@'localhost' identified by 'jageRmB2';
create user 'fzhou';
create user webapp@server1 identified by 'apple';
grant all privileges on *.* to 'fzhou';
select user();
```

### ddl
```
create database dbname;
drop database dbname;
use dbname;
create table example (...);
drop table tablename;
```

### dml
```
select user from mysql.user group by user;
mysql -u username -p < example.sql
```

### check db / table info
```
show databases;
show tables;
show table status;
show index from table_name;
show create table table_name;
show procedure status where db = 'airflow';

show processlist;
```

### backup
```
mysqldump -u root -p database_name > backup_file_name
```
