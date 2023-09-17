package com.yjntc.excelexporter.common.constant;


import com.yjntc.excelexporter.common.Result;
import com.yjntc.excelexporter.exception.ResultException;

/**
 *  结果快速构建器
 * @author wangkangsheng
 */
public interface ResultBuilder {

    int MAX_PAGE = 1000;


    default <T> Result<T> success(T data){
        return Result.buildResult(ApiEnum.SUCCESS,"ok",data);
    }

    default <T> Result<T> error(ApiEnum result){
        return Result.buildResult(result);
    }

    default <T> Result<T> error(int code,String msg){
        return Result.buildResult(code, msg);
    }

    default <T> Result<T> error(ApiEnum result,String msg){
        return Result.buildResult(result,msg);
    }
    default <T> Result<T> error(ApiEnum result,String msg,T data){
        return Result.buildResult(result,msg,data);
    }

    default ResultException exception(ApiEnum resultCode){
        return new ResultException(error(resultCode));
    }

    default ResultException exception(int code,String msg){
        return new ResultException(error(code,msg));
    }

    default ResultException exception(ApiEnum resultCode,String msg){
        return new ResultException(error(resultCode,msg));
    }

}
