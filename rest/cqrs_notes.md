# CQRS notes
* CQRS: Command Query Responsibility Segregation
* separates write (commands) from read (queries)
    * example: SQL db for writing, on-SQL db for reading.

## pro and cons
* efficient scaling
* needs a variety of databases
* data consistency requirement (CAP)


## side notes:

### CAP
#### concept
* Consistency: Every read receives the most recent write or an error.
* Availability: always response (may not be the latest)
* Partition (cluster / network) tolerance: message lost
* -- note: scalability is not discussed here.

#### usage
* CA: normal / standalone system
* DBMS / ACID = Consistency (C) > Availability (A)
* NoSQL: Availability (A) > Consistency (C)

### DDD oriented microservice
* Cohesion is key within a single bounded context.