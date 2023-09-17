package com.yjntc.excelexporter.excel.def.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import com.yjntc.excelexporter.excel.ifce.ExcelTypeHandler;
import com.yjntc.excelexporter.excel.ExcelMeta;
import com.yjntc.excelexporter.excel.FieldMapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-29 19:53
 */
public class DateTypeHandler implements ExcelTypeHandler<Date> {

    /**
     * 无效的
     */
    private static final Set<String> INVALID_PATTERN = new HashSet<>();
    /**
     * 有效的
     */
    private static final Set<String> VALID_PATTERN = new HashSet<>();

    /**
     * 获取待处理的类型
     *
     * @return Class<T>
     */
    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    /**
     * 序列化为字符串
     *
     * @param date
     *         实体对象
     * @param fm
     *         字段映射信息
     * @param meta
     *         excel元信息
     *
     * @return String 序列的字符串
     */
    @Override
    public String serialize(Date date, FieldMapping fm, ExcelMeta meta) {
        String pattern = DatePattern.NORM_DATETIME_PATTERN;
        if (Validator.isNotEmpty(fm.getDateFormat()) && !INVALID_PATTERN.contains(pattern) && !VALID_PATTERN.contains(pattern)){
            pattern = fm.getDateFormat();
            try {
                DatePattern.createFormatter(pattern);
                VALID_PATTERN.add(pattern);
            }catch (Exception e){
                System.err.println("无效的时间格式化表达式["+pattern+"]");
                INVALID_PATTERN.add(pattern);
                pattern = DatePattern.NORM_DATETIME_PATTERN;
            }
        }else if (VALID_PATTERN.contains(pattern)){
            pattern = fm.getDateFormat();
        }else if (INVALID_PATTERN.contains(pattern)){
            // pass
            pattern = DatePattern.NORM_DATETIME_PATTERN;
        }
        return DateUtil.format(date,pattern);
    }

}
