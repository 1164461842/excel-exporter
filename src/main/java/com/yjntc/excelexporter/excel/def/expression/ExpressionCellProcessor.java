package com.yjntc.excelexporter.excel.def.expression;

import cn.hutool.core.lang.Validator;
import com.yjntc.excelexporter.excel.ifce.processor.CellProcessor;
import com.yjntc.excelexporter.excel.FieldMapping;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.HashMap;
import java.util.Map;

/**
 *  表达式处理器
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-30 13:47
 */
public class ExpressionCellProcessor implements CellProcessor {

    /**
     * 表达式缓存
     */
    private static final Map<String , Expression> EXPRESSION_CACHE = new HashMap<>();
    /**
     * 为空的默认内容
     */
    private static final String DEF_EP_KEY = "def";
    /**
     * 不为空 且都不匹配时其他内容
     */
    private static final String OTHER_EP_KEY = "other";

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
        String expression = fm.getExpression();
        if (Validator.isEmpty(fm.getDict()) && Validator.isNotEmpty(expression)){
            Expression epObj = EXPRESSION_CACHE.get(expression);
            if (null == epObj){
                epObj = parseExpression(expression);
                EXPRESSION_CACHE.put(expression,epObj);
            }

            cell.setCellValue(epObj.parse(cell.getStringCellValue()));
        }
    }

    /**
     * 解析表达式
     * @param expression String 表达式字符串
     * @return Map<String,String>
     */
    private Expression parseExpression(String expression){
        String[] epItems = expression.split(" ");
        Expression epObj = new Expression();
        Map<String,String> map = new HashMap<>();
        for (String item : epItems) {
            String[] kv = item.split("=");
            if (kv.length == 2){
                String k = kv[0];
                String v = kv[1];
                if (DEF_EP_KEY.equals(k)){
                    epObj.setDef(v);
                }else if (OTHER_EP_KEY.equals(k)){
                    epObj.setOther(v);
                }else {
                    map.put(k,kv[1]);
                }
            }
        }
        epObj.setExpression(expression);
        epObj.setValMap(map);
        return epObj;
    }

    private static final class Expression{
        private String expression;
        private String def;
        private String other;
        private Map<String,String> valMap;

        public String getExpression() {
            return this.expression;
        }

        public String getDef() {
            return this.def;
        }

        public String getOther() {
            return this.other;
        }

        public Map<String, String> getValMap() {
            return this.valMap;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public void setDef(String def) {
            this.def = def;
        }

        public void setOther(String other) {
            this.other = other;
        }

        public void setValMap(Map<String, String> valMap) {
            this.valMap = valMap;
        }

        public String parse(String raw){
            String mapData = getValMap().get(raw);
            if (null != mapData){
                return mapData;
            }
            if (Validator.isNotEmpty(raw) && Validator.isNotEmpty(getOther())){
                return getOther();
            }else if (Validator.isEmpty(raw) && Validator.isNotEmpty(getDef())){
                return getDef();
            }
            return raw;
        }
    }

}
