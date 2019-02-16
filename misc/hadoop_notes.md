# ----------------------------------------------
#  Hadoop notes.
#    HDFS + MapReduce
# ----------------------------------------------

## Distributed Storage:
# ----------------------------------------------
S3 – Non urgent batch jobs. S3 fits very specific use cases, when data locality isn’t critical.
Cassandra – Perfect for streaming data analysis and an overkill for batch jobs.
HDFS – Great fit for batch jobs without compromising on data locality.


## HDFS
# ----------------------------------------------

Datanode - where HDFS actually stores the data, there are usually quite a few of these.
Namenode - the ‘master’ machine. 

# ist files in the root directory
hadoop fs -ls /
hadoop fs -ls ./

# cat a file (decompressing if needed)
hadoop fs -text ./file.txt.gz #

# upload and retrieve a file
hadoop fs -put ./localfile.txt /home/matthew/remotefile.txt
hadoop fs -get /home/matthew/remotefile.txt ./local/file/path/file.txt


## MapReduce
# ----------------------------------------------

Map tasks perform a transformation.
Reduce tasks perform an aggregation.

# Example:
def map(lineNumber: Long, sentance: String) = {
  val words = sentance.split()
  words.foreach{word =>
    output(word, 1)
  }
}

def reduce(word: String, counts: Iterable[Long]) = {
  var total = 0l
  counts.foreach{count =>
    total += count
  }
  output(word, total)
}
-- Notice that the output to a map and reduce task is always a KEY, VALUE pair. 

# Scheduler: Job Tracker (JT) and the Task Tracker (TT). 


## Hadoop Architecture
  Hadoop Common: These are Java libraries and utilities required by other Hadoop modules. 
  Hadoop YARN: This is a framework for job scheduling and cluster resource management.
  Hadoop Distributed File System (HDFS™): A distributed file system that provides high-throughput access to application data.
  Hadoop MapReduce: This is YARN-based system for parallel processing of large data sets.


## Pig
# ----------------------------------------------


High-level platform for creating programs that run on Apache Hadoop. 
Pig Latin can be extended using User Defined Functions (UDFs) which the user can write in Java, Python, JavaScript, Ruby or Groovy.

~/tmp
also check /data directory
pig -v -x local -f llayer_test.pig

## HBase
# ----------------------------------------------

Being a FS, HDFS lacks the random read/write capability. 
HBase is a NoSQL database that runs on top of Hadoop cluster and provides random real-time read/write access.
