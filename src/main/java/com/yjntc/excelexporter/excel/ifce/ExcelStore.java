package com.yjntc.excelexporter.excel.ifce;

import java.io.IOException;
import java.io.InputStream;

/**
 *  excel存储器
 * @author WangKangSheng
 * @date 2022-04-28 18:26
 */
public interface ExcelStore {

    /**
     * 存储excel
     * @param is InputStream 输入流
     * @return String 返回文件地址 或文件信息
     */
    void store(InputStream is) throws IOException;

}
