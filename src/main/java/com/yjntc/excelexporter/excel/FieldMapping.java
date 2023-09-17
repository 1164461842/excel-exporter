package com.yjntc.excelexporter.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *  字段映射信息
 * @author WangKangSheng
 * @date 2022-04-28 18:08
 */
@Setter
@Getter
@AllArgsConstructor
@Accessors(chain = true)
public class FieldMapping {

    /**
     * java 字段的名称
     */
    private String fieldName;

    /**
     * excel 表头的名称
     */
    private String headerName;

    /**
     * 时间对象格式化的表达式
     */
    private String dateFormat;

    /**
     * 列顺序
     *  默认99
     */
    private Integer seq;

    /**
     * 字典名
     */
    private String dict;

    /**
     * 表达式 优先级最低
     *  格式 每个映射字段之间用空格隔开
     *  1=男 2=女 def=男 other=未知
     *
     *  def=xxx
     *      空值的之后的默认值
     *  other=xxx
     *      都不匹配的值
     */
    private String expression;


    public FieldMapping(String fieldName, String headerName, Integer seq) {
        this.fieldName = fieldName;
        this.headerName = headerName;
        this.seq = seq;
    }

    public FieldMapping(String fieldName, String headerName, String dateFormat, Integer seq) {
        this.fieldName = fieldName;
        this.headerName = headerName;
        this.seq = seq;
        this.dateFormat = dateFormat;
    }

    public FieldMapping(String fieldName, String headerName) {
        this.fieldName = fieldName;
        this.headerName = headerName;
    }

    public FieldMapping() {
    }
}
