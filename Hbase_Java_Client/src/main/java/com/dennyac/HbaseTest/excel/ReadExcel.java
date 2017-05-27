package com.dennyac.HbaseTest.excel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by shuyun on 2016/10/28.
 */
public class ReadExcel {

    public static void main(String[] srgs){
        String filePath = "F:\\datafortag\\1212.xls";
        HSSFWorkbook wb = null;
        POIFSFileSystem fs = null;
        try {
            fs = new POIFSFileSystem(new FileInputStream(new File(filePath)));
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {

        }

        HSSFSheet sheet = wb.getSheetAt(0);
        System.out.println("excel共有数据：" + sheet.getLastRowNum());
        for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++){
            HSSFRow row = sheet.getRow(rowNum);
            System.out.print("第" + rowNum + "行数据：");
            for (int cellNum = row.getFirstCellNum() ; cellNum < row.getLastCellNum(); cellNum++) {
                Cell cell = row.getCell(cellNum);
                System.out.print(cell.getStringCellValue() + "   ");
                if(cell.getStringCellValue().equals("立即购买")) {
                    System.out.print(cell.getHyperlink().getAddress() + "   ");
                }
            }
            System.out.println();
        }
    }
}
