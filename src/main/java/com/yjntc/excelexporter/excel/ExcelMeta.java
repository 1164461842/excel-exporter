package com.yjntc.excelexporter.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *  excel导出元信息
 * @author WangKangSheng
 * @date 2022-04-28 18:17
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExcelMeta {
    /**
     * excel名称 不需要添加后缀名
     */
    private String excelName;

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 排序的
     *  如果sortable为true 则fieldMapping的seq字段不生效 按照传入的顺序写入excel
     */
    private Boolean sortable;

    /**
     * 字段映射信息
     */
    private List<FieldMapping> fieldMappings;

}
