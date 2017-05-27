package com.dennyac.HbaseTest.mysql;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Created by shuyun on 2016/8/26.
 */
public class TestMysql {

    private static FileOutputStream fileOutputStream = null;
    //fileOutputStream = new FileOutputStream((file));
    private static OutputStreamWriter outputStreamWriter = null;
    private static BufferedWriter bufferedWriter = null;

    public static void main(String[] args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://jconnyxynbbjh.mysql.rds.aliyuncs.com/odps_results";
            conn = DriverManager.getConnection(url, "odpsresults", "lkejfioqpx334Fk");
            //stmt = conn.createStatement();
            System.out.println("成功连接MySQL驱动程序");

            ps = conn.prepareStatement("select * from dw_order_tmp",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();
            //rs = stmt.executeQuery("select * from dw_trade_tmp limit 10");
            //System.out.println("共有" + GetNumRows(rs) + "条数据");

//            File file = new File("/home/xiuyuan.huang/test-es-mysql/test.txt");
//            FileOutputStream fileOutputStream = null;
//            fileOutputStream = new FileOutputStream((file));
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            File file = new File("/home/xiuyuan.huang/test-es-mysql/1_dw_order_tmp.txt");
            createFile(file);

            //检索此 ResultSet 对象的列的编号、类型和属性。
            ResultSetMetaData rsmd = rs.getMetaData();
            //得到当前的列数
            int colCount = rsmd.getColumnCount();
            Integer count = 0;
            while(rs.next())  {  //while控制行数
                StringBuffer buffer = new StringBuffer();
                for(int i = 1; i <= colCount; i++  )   {//for循环控制列数
                    //得到当前列的列名
                    String name = rsmd.getColumnName(i);
                    //得到当前列的值
                    String value = rs.getString(i);
                    buffer.append("\"" + name  + "\":\""  + value + "\"");
                    if(i < colCount){
                        buffer.append(",");
                    }
                    //bufferedWriter.write("\"" + name  + "\"=\""  + value + "\",");
                }
                count++;

                if(count % 10000000 == 0){
                    bufferedWriter.flush();
                    file = new File("F:\\datafortag\\" + count+1 + "_dw_order_tmp" + ".txt");
                    createFile(file);
                }

                if(count % 100000 == 0){
                    System.out.println("写入" + count + "条数据");
                }
                bufferedWriter.write("{" + buffer +"}");
                bufferedWriter.newLine();
            }




//            while (rs.next()) {
//
//                //System.out.println(rs.getString(1) +" "+ rs.getString("tid"));
//                bufferedWriter.write(rs.getString(1) +" "+ rs.getString("tid"));
//                bufferedWriter.newLine();
//            }
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createFile(File file) {
        try {
            //File file = new File("/home/xiuyuan.huang/test-es-mysql/test.txt");
            fileOutputStream = new FileOutputStream((file));
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    private static Integer GetNumRows(ResultSet rs) throws Exception {
        //通过改方法获取结果集的行数
        Integer result = 0;
        if (rs.last()) {
            result = rs.getRow();
            rs.beforeFirst();//光标回滚
        }
        return result;
    }

}