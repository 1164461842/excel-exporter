package com.yjntc.excelexporter.excel.ifce.processor;

import com.yjntc.excelexporter.excel.FieldMapping;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.List;

/**
 *  插槽方法
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-29 16:35
 */
public interface RowProcessor {

    /**
     * 处理方法
     * @param sheet 当前表
     * @param row 当前行
     * @param rowNum 当前行数
     * @param fms 行映射器
     */
    void process(SXSSFSheet sheet, SXSSFRow row, int rowNum, List<FieldMapping> fms);

}
