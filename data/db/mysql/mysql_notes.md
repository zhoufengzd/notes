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
grant select on *.* to '<user>'@'%' identified by '<default_pwd>';
grant all privileges on *.* to airflow identified by 'pwd';
set password='blabla'; -- change own password

create user <user> identified by '<default_pwd>';
grant all privileges on *.* to '<user>';
select user(); -- check current logon user

select host, user from mysql.user;
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
