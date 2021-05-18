# Hadoop: HDFS + MapReduce
* fault tolerance
* reliability
* scalability
* high availability

## Overview distributed storage:
* S3 – Non urgent batch jobs. S3 fits very specific use cases, when data locality is not critical.
* Cassandra – Perfect for streaming data analysis and an overkill for batch jobs.
* HDFS – Great fit for batch jobs without compromising on data locality.

## Hadoop vs RDBMS
* usage: OLTP <-> Analytics
* schema:
    * rdbms: required on write (static)
    * hadoop: required on reading (dynamic)
* fast write / slow read <-> fast read
* cost: low <-> high

## HDFS
* Datanode: where data is stored
* Namenode: the ‘master’ machine.

## Commands:
* list files in the root directory
```
hadoop fs -ls /
hadoop fs -ls ./
```
* cat a file (decompressing if needed)
`hadoop fs -text ./file.txt.gz`
* upload and retrieve a file
```
hadoop fs -put ./localfile.txt /home/matthew/remotefile.txt
hadoop fs -get /home/matthew/remotefile.txt ./local/file/path/file.txt
```

## MapReduce
* Map tasks perform a transformation.
* Reduce tasks perform an aggregation.
* example:
```
def map(lineNumber: Long, sentance: String) = {
  val words = sentance.split()
  words.foreach{word =>
    output(word, 1)
  }
}
```
```
def reduce(word: String, counts: Iterable[Long]) = {
  var total = 0l
  counts.foreach{count =>
    total += count
  }
  output(word, total)
}
```


## Scheduler: Job Tracker (JT) and the Task Tracker (TT).

## Hadoop Architecture
* Hadoop Common: Java utilities libraries
* Hadoop YARN: for job scheduling and cluster resource management.
* Hadoop Distributed File System (HDFS™): A distributed file system
* Hadoop MapReduce: YARN-based parallel processing system

## Hadoop in docker
* single node: sequenceiq/hadoop-docker
* multiple nodes:
`git clone git@github.com:big-data-europe/docker-hadoop.git`

## Extensions
### Pig
* High-level platform to interact with Apache Hadoop.
* User Defined Functions (UDFs) supported

### HBase
* context: HDFS lacks the random read/write capability.
* NoSQL database on Hadoop
* support random real-time read/write access.
