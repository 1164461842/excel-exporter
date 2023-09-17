package com.yjntc.excelexporter.controller;

import com.yjntc.excelexporter.common.Result;
import com.yjntc.excelexporter.common.constant.ApiEnum;
import com.yjntc.excelexporter.common.constant.ResultBuilder;
import com.yjntc.excelexporter.entity.RoomV2;
import com.yjntc.excelexporter.excel.def.dict.DictCellProcessor;
import com.yjntc.excelexporter.excel.def.processor.ProcessorHelper;
import com.yjntc.excelexporter.req.house.UnitIdExportReq;
import com.yjntc.excelexporter.excel.ExcelMeta;
import com.yjntc.excelexporter.excel.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author WangKangSheng
 * @date 2022-04-28 17:32
 */
@RestController
@RequestMapping("/house/v1")
public class DemoController implements ResultBuilder {
    @Autowired
    private DictCellProcessor dictCellProcessor;


    /**
     * 导出人口数据
     * @param req PersonFullQueryExportReq
     * @return Result<String>
     */
    @PostMapping("/export")
    public Result<String> export(@RequestBody UnitIdExportReq req){

        ExcelMeta excelMeta = req.getExcelMeta();
        ExcelWriter<RoomV2> excelWriter = new ExcelWriter<>();
        AtomicReference<String> fid = new AtomicReference<>();
        try {
            excelWriter
            .addAfterHeaderCellProcessor(ProcessorHelper.defTitleStyleProcessor())
            .addAfterCellProcessor(dictCellProcessor)
            .addAfterCellProcessor(ProcessorHelper.expressionCellProcessor())
            .write(excelMeta,
                    readTimes -> {
                        // read times  为读取数据的次数 可实现分页数据读取
                        // 最终返回null时即可停止读取数据

                        if (readTimes > 1){
                            return null;
                        }else{
                            return new ArrayList<>();
                        }
                    },
                    // 存储到文件 或 使用输入流存储
                    is -> {
                        // is 输入流

                    });
        }catch (IOException ioe){
            ioe.printStackTrace();
            return error(ApiEnum.RUNTIME_ERROR,"导出失败");
        }

        return success(fid.get());
    }

}
