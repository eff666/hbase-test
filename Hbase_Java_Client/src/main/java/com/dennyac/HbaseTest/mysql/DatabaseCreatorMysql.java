package com.dennyac.HbaseTest.mysql;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shuyun on 2016/8/26.
 */
public class DatabaseCreatorMysql{

//    public void createDatabaseAndInitialize(DriverConnectionProvider cp,
//                                            String databaseName, String sourceFilePath) {
//        createDatabase(cp,databaseName);
//        initializeDatabase(cp,databaseName,sourceFilePath);
//    }

//    public void createDatabase(ConnectionProvider cp,String databasename){
//        Connection con = cp.getConnection();
//        try {
//            con.createStatement().execute("create database "+databasename);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public void initializeDatabase(String databaseName,String sourceFilePath){
        try {
            String username = "odpsresults";
            String password = "lkejfioqpx334Fk";

            StringBuffer sb = new StringBuffer("cmd.exe /C mysql -u");
            sb.append(username);

            if(password!=null && !password.equalsIgnoreCase(""))
                sb.append(" -p").append(password);

            sb.append(" ").append(databaseName).append("<").append(sourceFilePath);
            //Process proc = java.lang.Runtime.getRuntime().exec("cmd.exe /C mysql -uroot test2<d:/erp.sql");
            Process proc = java.lang.Runtime.getRuntime().exec(sb.toString());

            BufferedInputStream bis = new BufferedInputStream(proc.getErrorStream());
            InputStreamReader inputStreamReader = new InputStreamReader(bis,"GBK");
            BufferedReader br = new BufferedReader(inputStreamReader);

            String line = null;
            while ( (line = br.readLine()) != null)
                System.out.println(line);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2","root","");
		Statement stmt = conn.createStatement();
		//stmt.executeUpdate("source  C:\\Users\\jiangkai\\Desktop\\erp.sql");
        stmt.execute("source  C:\\Users\\jiangkai\\Desktop\\erp.sql");


		String statement ="cmd.exe mysql -uroot -p use test2;source C:/Users/jiangkai/Desktop/erp.sql";
		//String statement ="javac";
		String[] statements={
				"cmd.exe /C cmd /C mysqldump -u root -p erp>d:/test.sql",
				"cmd /c mysql -u root",
				"source C:/Users/jiangkai/Desktop/erp.sql",
				"cmd.exe /C mysqldump -uroot erp>d:/test.sql"
		};

		Process proc = java.lang.Runtime.getRuntime().exec("cmd.exe /C mysql -uroot test2<d:/erp.sql");
		System.out.println("gogogo");
		InputStream stderr=proc.getErrorStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stderr));


        //new String(line.getBytes(),"gb2312")

        //String mysql="mysqladmin -uroot -proot create databasename";
		String mysql="mysqladmin -uroot -proot use test2";
		java.lang.Runtime.getRuntime().exec("cmd /c "+mysql);
		String path="mysql test2 < C:/Users/jiangkai/Desktop/erp.sql";
		java.lang.Runtime.getRuntime().exec("cmd /c "+path);
    }


}
