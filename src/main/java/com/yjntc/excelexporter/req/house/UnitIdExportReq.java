package com.yjntc.excelexporter.req.house;

import com.yjntc.excelexporter.req.idreq.UnitIdReq;
import com.yjntc.excelexporter.excel.ExcelMeta;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-07 10:42
 */
@Setter
@Getter
public class UnitIdExportReq extends UnitIdReq {


    /**
     * excel导出元信息
     */
    @NotNull(message = "导出信息不能为空")
    private ExcelMeta excelMeta;

}
