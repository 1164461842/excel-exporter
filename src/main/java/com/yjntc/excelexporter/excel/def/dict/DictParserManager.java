package com.yjntc.excelexporter.excel.def.dict;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *  字典解析管理器
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-30 11:21
 */
@Component
public class DictParserManager {

    private static final List<DictParser> PARSERS = new ArrayList<>();

    public static void register(DictParser parser){
        PARSERS.add(parser);
    }

    public DictParser getParser(String dictName){
        for (DictParser parser : PARSERS) {
            boolean b = parser.canParse(dictName);
            if (b){
                return parser;
            }
        }
        return null;
    }

}
