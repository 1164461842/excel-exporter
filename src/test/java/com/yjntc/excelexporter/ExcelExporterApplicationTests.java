package com.yjntc.excelexporter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.*;

// @SpringBootTest
class ExcelExporterApplicationTests {

    /**
     * 文件输出根目录
     */
    private static final String PATH_ROOT = "J:\\files";

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        exportTest();
        // SXSSFWorkbook workbook = new SXSSFWorkbook();
    }

    private MongoTemplate mongoTemplate;
    private GridFsTemplate gridFsTemplate;

    // @Test
    public static void exportTest(){
        // xls
        // HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // xlsx
        // XSSFWorkbook workbook = new XSSFWorkbook();

        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // cell创建帮助工具
        CreationHelper createHelper = workbook.getCreationHelper();
        String sheetName = WorkbookUtil.createSafeSheetName("sheet1");
        SXSSFSheet sheet1 = workbook.createSheet(sheetName);
        long x = System.currentTimeMillis();
        for (int j = 0; j < 1000000; j++) {
            SXSSFRow row = sheet1.createRow(j);
            for (int i = 0; i < 60; i++) {
                row.createCell(i).setCellValue(createHelper.createRichTextString("""
                        66666$""" + j + " -- " + i));
            }
        }


        // sheet1.flushRows();
        ByteArrayOutputStream ops = null;
        try {
            ops = new ByteArrayOutputStream();
            workbook.write(ops);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(ops.toByteArray());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(null != workbook){
                    workbook.dispose();
                }
                if (null != ops){
                    ops.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println((System.currentTimeMillis() - x ) / 1000 );

    }


}
