# hadoop-inverted-index

## Inverted Index using Hadoop(Map/Reduce) on GCP
* create inverted index of words from the given subset of project gutenberg corpus
* inverted index implementation using map-reduce (hadoop) job in java
* job is submitted on google cloud platform (gcp) using cloud dataproc

### InvertedIndexJob.java
* code ref1: https://developer.yahoo.com/hadoop/tutorial/module4.html#wordcount
* code ref2: https://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html
* need to convert job java file as jar (see below)

### sample.txt
* inverted index result for sample text "The American by Henry James" (6818880)

### Useful Commands for GCP hadoop
```
Create home directory on HDFS
hadoop fs -mkdir -p /user/<your username here>

Set up env var for JAVA and HADOOP_CLASSPATH
export PATH=${JAVA_HOME}/bin:${PATH}
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar

Check env var using
env

Check if correctly setup using
hadoop fs -ls

Creating jar for code to run as Map-Reduce (Hadoop) job
hadoop com.sun.tools.javac.Main InvertedIndexJob.java
jar cf invertedindex.jar InvertedIndex*.class

hadoop fs -copyFromLocal -f ./invertedindex.jar
hadoop fs -cp ./invertedindex.jar gs://dataproc.../JAR

Merge ouput files
hadoop fs -getmerge gs://dataproc.../output ./output.txt
hadoop fs -copyFromLocal ./output.txt
hadoop fs -cp ./output.txt gs://dataproc.../output.txt
```
