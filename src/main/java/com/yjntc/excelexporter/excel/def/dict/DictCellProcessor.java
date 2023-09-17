package com.yjntc.excelexporter.excel.def.dict;

import cn.hutool.core.lang.Validator;
import com.yjntc.excelexporter.excel.ifce.processor.CellProcessor;
import com.yjntc.excelexporter.excel.FieldMapping;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *  字典行处理器
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-30 11:10
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DictCellProcessor implements CellProcessor {

    private static final Map<String,Map<String,String>> DICT_CACHE = new HashMap<>();
    private static final Map<String,DictParser> DICT_PARSER_CACHE = new HashMap<>();

    /**
     * 处理方法
     *
     * @param sheet
     *         当前表
     * @param row
     *         当前行
     * @param cell
     *         当前的单元格
     * @param cellNum
     *         单元格索引
     * @param fm
     *         行映射器
     */
    @Override
    public void process(SXSSFSheet sheet, SXSSFRow row, Cell cell, int cellNum, FieldMapping fm) {
        String dict = fm.getDict();
        if (Validator.isNotEmpty(dict)){
            DictParser dictParser = DICT_PARSER_CACHE.get(dict);
            if (null != dictParser){
                // 可以解析字典 并设置对应的值
                String dictVal = dictParser.parseValue(cell.getStringCellValue(), dict, DICT_CACHE.get(dict));
                cell.setCellValue(dictVal);
            }
        }
    }

}
