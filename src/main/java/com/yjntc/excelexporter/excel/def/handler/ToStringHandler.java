package com.yjntc.excelexporter.excel.def.handler;

import com.yjntc.excelexporter.excel.ifce.ExcelTypeHandler;
import com.yjntc.excelexporter.excel.ExcelMeta;
import com.yjntc.excelexporter.excel.FieldMapping;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-29 16:24
 */
public class ToStringHandler implements ExcelTypeHandler<Object> {

    /**
     * 获取待处理的类型
     *
     * @return Class<T>
     */
    @Override
    public Class<Object> getType() {
        return Object.class;
    }

    /**
     * 序列化为字符串
     *
     * @param o
     *         实体对象
     * @param fm
     *         字段映射信息
     * @param meta
     *         excel元信息
     *
     * @return String 序列的字符串
     */
    @Override
    public String serialize(Object o, FieldMapping fm, ExcelMeta meta) {
        return o.toString();
    }

}
