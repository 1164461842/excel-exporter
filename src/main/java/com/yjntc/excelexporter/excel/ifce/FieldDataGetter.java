package com.yjntc.excelexporter.excel.ifce;

import org.apache.poi.ss.formula.functions.T;

/**
 *  字段读取器
 * @author WangKangSheng
 * @date 2022-04-28 18:07
 */
public interface FieldDataGetter<T> {

    /**
     * 从实体类读取字段内容
     * @param bean 实体类
     * @param field String 字段
     * @return Object object
     */
    Object getFromBean(T bean,String field);


}
