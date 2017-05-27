package com.dennyac.HbaseTest.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by shuyun on 2016/8/31.
 */
public class UnZip {
    public static  void visitTARGZ(File targzFile) throws IOException {
        FileInputStream fileIn = null;
        BufferedInputStream bufIn = null;
        GZIPInputStream gzipIn = null;
        TarArchiveInputStream taris = null;
        try {
            fileIn = new FileInputStream(targzFile);
            bufIn = new BufferedInputStream(fileIn);
            gzipIn = new GZIPInputStream(bufIn); // first unzip the input file
            // stream.
            taris = new TarArchiveInputStream(gzipIn);
            TarArchiveEntry entry = null;
            while ((entry = taris.getNextTarEntry()) != null) {
//                System.out.println(entry);
//                String  fileName = entry.getName();
//                System.out.println(fileName);
//                FileInputStream fileInputStream = new FileInputStream(fileName);
//                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String json = "";
//                while((json = bufferedReader.readLine()) != null){
//                    System.out.println(json);
//                }
//                String name = entry.getName();
//                String[] names = name.split("/");
//                String fileName = outputDirectory;
//                for(int i = 0;i< names.length;i++){
//                    String str = names[i];
//                    fileName = fileName + File.separator + str;
//                }
//                if (name.endsWith("/")) {
//                    mkFolder(fileName);
//                } else {
//                    File file = mkFile(fileName);
//                    bufferedOutputStream = new BufferedOutputStream(
//                            new FileOutputStream(file));
//                    int b;
//                    while ((b = bufferedInputStream.read()) != -1) {
//                        bufferedOutputStream.write(b);
//                    }
//                    bufferedOutputStream.flush();
//                    bufferedOutputStream.close();
//                }
//                entry = (TarArchiveEntry) in.getNextEntry();
//            }


            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            taris.close();
            gzipIn.close();
            bufIn.close();
            fileIn.close();
        }
    }

    public static void main(String[] args) {
        try {
            visitTARGZ(new File("F:\\datafortag\\datafortag.tar.gz"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
