# System Design
* goal / target / core features
* scale:
    * bottleneck
* priority:
    * delivery time
    * reliability
    * cost
* assumptions:

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

## Geo Partition
* R-tree (rectangle tree)
    * The key challenge: balanced, not too empty, minimum overlap
* index of longitude / latitude

## API Design

## references:
* https://igotanoffer.com/blogs/tech/system-design-interviews
