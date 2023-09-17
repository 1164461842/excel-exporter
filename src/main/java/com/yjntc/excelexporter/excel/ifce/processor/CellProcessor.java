package com.yjntc.excelexporter.excel.ifce.processor;

import com.yjntc.excelexporter.excel.FieldMapping;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;

/**
 *  插槽方法
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-29 16:35
 */
public interface CellProcessor {

    /**
     * 处理方法
     * @param sheet 当前表
     * @param row 当前行
     * @param cell 当前的单元格
     * @param cellNum 单元格索引
     * @param fm 行映射器
     */
    void process(SXSSFSheet sheet, SXSSFRow row, Cell cell, int cellNum, FieldMapping fm);

}
