package com.yjntc.excelexporter.excel.ifce;

import java.util.List;

/**
 *  数据提供者
 * @author WangKangSheng
 * @date 2022-04-28 18:30
 */
public interface DataProvider<T> {

    /**
     * 加载数据的方法
     *  如果返回的是null 或者 list.isEmpty() 为true时即停止继续获取,
     *   否则会一直调用此方法获取数据
     * @param readTimes 当前读取次数 可理解为分页中的当前页数
     * @return List<T> 数据列表
     */
    List<T> obtainData(int readTimes);

}
