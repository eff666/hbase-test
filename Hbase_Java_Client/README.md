# About

This repository contains a few examples of how to connect to HBase and fetch data using the Java Client API.


# Instructions

1. Clone this repository
2. Run the following commands in command line within the repository- 

<pre><code>
> cd Hbase_Java_Client
> mvn clean
> mvn package
> cd target
> java -cp HbaseTest-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.dennyac.HbaseTest.GetExample
> java -cp HbaseTest-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.dennyac.HbaseTest.ScanExample
</code></pre>
