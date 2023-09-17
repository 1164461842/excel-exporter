package com.yjntc.excelexporter.excel.def.processor;

import com.yjntc.excelexporter.excel.ifce.processor.CellProcessor;
import com.yjntc.excelexporter.excel.def.expression.ExpressionCellProcessor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-29 17:54
 */
public class ProcessorHelper {

    private static final CellProcessor EXPRESSION_PROCESSOR = new ExpressionCellProcessor();

    /**
     * 默认标题样式处理器
     * @return CellProcessor
     */
    public static CellProcessor defTitleStyleProcessor(){
        return (sheet, row,cell, rowNum, fms) -> {
            SXSSFWorkbook workbook = sheet.getWorkbook();
            CellStyle cellStyle = row.getRowStyle();
            if (null == cellStyle){
                cellStyle = workbook.createCellStyle();
            }
            // 字体样式
            Font font = workbook.createFont();
            // font.setColor(IndexedColors.WHITE.getIndex());

            // 字体
            cellStyle.setFont(font);

            // 背景
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

            // 边框
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // 居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            cell.setCellStyle(cellStyle);
        };
    }


    public static CellProcessor expressionCellProcessor(){
        return EXPRESSION_PROCESSOR;
    }

}
