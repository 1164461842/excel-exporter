package com.yjntc.excelexporter.excel.def.dict;

import java.util.Map;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-30 11:17
 */
public interface DictParser {

    /**
     * 是否可以处理该字典
     * @param dictName String 字典名称
     * @return Map<String,String>
     */
    boolean canParse(String dictName);

    /**
     * 解析字典
     * @return 字典值-字典value
     */
    Map<String,String> parse(Object data);

    /**
     * 解析值
     * @param key String 字典值
     * @param dictName String 字典名称
     * @param dictData Map<String,String> 字典内容 由parse产生
     * @return String
     */
    String parseValue(String key,String dictName,Map<String,String> dictData);
}
