package com.yjntc.excelexporter.exception;


import com.yjntc.excelexporter.common.Result;

/**
 *  带有响应结果异常
 * @author wangkangsheng
 */
public class ResultException extends RuntimeException{

    private final Result<?> result;

    public ResultException(Result<?> result) {
        super(result.getResMsg());
        this.result = result;
    }

    public Result<?> getResult() {
        return result;
    }



}
