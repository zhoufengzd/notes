# System Design

## Load-balancing
* tolerance vs complexity setup
* nginx
* dns load balancing

## Caching
* Policy:
    * LRU: Least Recently Used
    * LFU: Least Frequently Used. Number of visits weighted.
* Core Data center / Smart Caching Site / micro datacenters
* 80 / 20 rule

## Database schema Design

## Slave-master replications

## Database sharding
* on multiple / remote servers
* reason:
    * to scale out (horizontally)
    * data segment: east region vs west region
* sharding key: balanced
* PostgreSql:
    * Foreign Data Wrapper (FDW)
    * Sharding on top of the partition

## API Design
