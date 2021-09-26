# kafka notes
* message queue: decouple producer / consumer
* performance: uses sequential disk I/O

## topics and partitions
* More Partitions Lead to Higher Throughput
    * cost: memory / cpu. recommendation < 50
* consumers <= partitions: or idle consumers.
* message ordering is per partiton
* partition may have copies
    * RF (Replication Factor): > 1
* each partition: leader / follower


## issues:
* Kafka rebalancing
    * consumer join or leave the group
