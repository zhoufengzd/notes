# MongoDB Notes

## Compared to sql:
* https://docs.mongodb.com/manual/reference/sql-comparison/
* database -> collection(table) -> document(row) -> field(column)
* join: lookup

## sharding
* replicating the schema, data allocated to different physical machines based on a shard key.
* each node shared nothing but replicated / copied
* vs Oracle RAC: data distributed, but share disk architecture

## command
* start service:
```
mongod --config <path-to-config-file>
```
* connect:
```
mongo --host 127.0.0.1:27017
mongo --host <hostname> --port <port>
```
* list database:
```
use admin;
db.adminCommand( { listDatabases: 1 } )
exit;

show databases;
show dbs;
```
* create db
```
use test
db
```
