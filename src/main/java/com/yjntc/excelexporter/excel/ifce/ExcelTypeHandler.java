package com.yjntc.excelexporter.excel.ifce;

import com.yjntc.excelexporter.excel.ExcelMeta;
import com.yjntc.excelexporter.excel.FieldMapping;

/**
 * @author WangKangSheng
 * @date 2022-04-28 18:10
 */
public interface ExcelTypeHandler<T> {

    /**
     * 获取待处理的类型
     * @return  Class<T>
     */
    Class<T> getType();

    /**
     * 序列化为字符串
     * @param t 实体对象
     * @param fm 字段映射信息
     * @param meta excel元信息
     * @return String 序列的字符串
     */
    String serialize(T t, FieldMapping fm, ExcelMeta meta);

}
