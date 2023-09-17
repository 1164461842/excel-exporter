package com.yjntc.excelexporter.excel.ifce;


import com.yjntc.excelexporter.excel.FieldMapping;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-29 16:10
 */
public interface TypeHandlerManager {

    /**
     * 获得处理器
     * @param o Object 对象
     * @param fm FieldMapping 字段信息
     * @return TypeHandler<?>
     */
    ExcelTypeHandler<?> getHandler(Object o, FieldMapping fm);

    /**
     * 注册类型处理器
     * @param eth ExcelTypeHandler<?> 类型处理器
     */
    void register(ExcelTypeHandler<?> eth);
}
