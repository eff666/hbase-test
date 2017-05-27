package com.dennyac.HbaseTest.HBase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class NewClientAPIExample extends Configured implements Tool {

    /** The identifier for the application table. */
    private static final TableName TABLE_NAME = TableName.valueOf("MyTable");
    /** The name of the column family used by the application. */

    public int run(String[] argv) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "172.29.1.10");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        setConf(conf);

        /** Connection to the cluster. A single connection shared by all application threads. */
        Connection connection = null;
        /** A lightweight handle to a specific table. Used from a single thread. */
        Table table = null;
        try {
            // establish the connection to the cluster.
            connection = ConnectionFactory.createConnection(getConf());
            // retrieve a handle to the target table.
            table = connection.getTable(TABLE_NAME);

            Scan scan = new Scan();
            System.out.println("Scan object instantiated");
            ResultScanner scanner = table.getScanner(scan);
            System.out.println("ResultScanner object instantiated");
            for(Result res: scanner){
                System.out.println(Bytes.toString(res.getRow()));
                System.out.println("Next iteration");
            }
        } finally {
            // close everything down
            if (table != null) table.close();
            if (connection != null) connection.close();
        }
        return 0;
    }

    public static void main(String[] argv) throws Exception {
        int ret = ToolRunner.run(new NewClientAPIExample(), argv);
        System.exit(ret);
    }
}