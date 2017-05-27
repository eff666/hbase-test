package com.dennyac.HbaseTest.HBase;

//cc GetExample Example application retrieving data from HBase
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
//import util.HBaseHelper;


import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.NavigableMap;

public class ScanExample {

	public static void main(String[] args) throws IOException {
//		System.setProperty("hadoop.home.dir", "f:/hadoop/hadoop-common-2.2.0-bin-master");

		/*Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "spark-25,spark-23,spark-27,spark-26,spark-24");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("zookeeper.znode.parent", "/hbase");*/
//		conf.addResource("f:/hadoop/hbase-site.xml");
//		conf.reloadConfiguration();
//		conf.set("hbase.master", "172.29.1.9:60000");



		System.out.print(System.getProperties().get("hadoop.home.dir"));
		// Connection to the cluster. A single connection shared by all application threads.
		Connection connection = null;
		// A lightweight handle to a specific table. Used from a single thread.
		Table table = null;
       
		ResultScanner scanner = null;
		try {
			// establish the connection to the cluster.
//			System.out.println(conf.get("zookeeper.znode.parent"));
			connection = HBaseUtil.getHbaseConnection();

			Admin admin = connection.getAdmin();
			TableName [] names = admin.listTableNames();
			for (TableName tableName : names) {
				System.out.println("Table Name is : " + tableName.getNameAsString());
			}

			table = connection.getTable(TableName.valueOf("tags_result"));
			Get get = new Get(Bytes.toBytes("00--自由女神-"));
			Result result = table.get(get);
			System.out.println(Bytes.toString(result.getRow()));

			NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> maps = result.getMap();

			for (Cell cell : result.listCells()) {
				System.out.print("family:" + Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength()) + " ");
				System.out.print("qualifier:" + Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength()) + " ");
				System.out.print("value:" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()) + " ");
				System.out.println();
			}


			/*Scan scan = new Scan();
			scan.setMaxResultSize(10);
			scan.setMaxResultsPerColumnFamily(2);
			System.out.println("Scan object instantiated");
			scanner = table.getScanner(scan);

			System.out.println("ResultScanner object instantiated");
			int i = 0;
			for(Result res: scanner){
				System.out.println(Bytes.toString(res.getRow()));
				System.out.println("Next iteration");
				if(++i == 10) break;
			}*/

			// retrieve a handle to the target table.

			/*HBaseAdmin admin = new HBaseAdmin(conf);
			String[] family = { "article", "author" };
			HTableDescriptor desc = new HTableDescriptor("");
			for (int i = 0; i < family.length; i++) {
				desc.addFamily(new HColumnDescriptor(family[i]));
			}

			admin.createTable(desc);
			System.out.println("create table Success!");*/
			/*table = connection.getTable(TableName.valueOf("top_order"));
			Scan scan = new Scan();
			System.out.println("Scan object instantiated");
			scanner = table.getScanner(scan);
			System.out.println("ResultScanner object instantiated");
			for(Result res: scanner){
				System.out.println(Bytes.toString(res.getRow()));
				System.out.println("Next iteration");
			}*/
		} finally {
			// close everything down
			if (scanner != null) scanner.close();
			if (table != null) table.close();
			if (connection != null) connection.close();
		}

		/*try {
			// establish the connection to the cluster.
//			System.out.println(conf.get("zookeeper.znode.parent"));
			connection = HBaseUtil.getHbaseConnection();

			Admin admin = connection.getAdmin();
			TableName[] names = admin.listTableNames();
			for (TableName tableName : names) {
				System.out.println("Table Name is : " + tableName.getNameAsString());
			}

			table = connection.getTable(TableName.valueOf("tags_result"));
			Get get = new Get(Bytes.toBytes("00--自由女神-"));
			Result result = table.get(get);
			System.out.println(Bytes.toString(result.getRow()));

			NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> maps = result.getMap();

			for (Cell cell : result.listCells()) {
				System.out.print("family:" + Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()) + " ");
				System.out.print("qualifier:" + Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()) + " ");
				System.out.print("value:" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()) + " ");
				System.out.println();
			}
		}
		finally {
			// close everything down
			if (scanner != null) scanner.close();
			if (table != null) table.close();
			if (connection != null) connection.close();
		}*/

		/*System.out.println("Initializing");
		Configuration conf = HBaseConfiguration.create();
		
		System.out.println("Created Configuration");
		conf.set("hbase.zookeeper.quorum", "172.29.1.10");
		conf.set("hbase.zookeeper.property.clientPort", "2181");

		System.out.println("Configuration created");
		HTable table = new HTable(conf, "top_order");
		System.out.println("HTable instantiated");
		
		Scan scan = new Scan();
		System.out.println("Scan object instantiated");
		ResultScanner scanner = table.getScanner(scan);
		System.out.println("ResultScanner object instantiated");
		for(Result res: scanner){
			System.out.println(Bytes.toString(res.getRow()));
			System.out.println("Next iteration");
		}
		
		scanner.close();
		System.out.println("Closed scanner");
		table.close();
		System.out.println("Closed table");*/
	}
}
