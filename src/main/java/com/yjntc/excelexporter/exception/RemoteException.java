package com.yjntc.excelexporter.exception;


import com.yjntc.excelexporter.common.Result;
import com.yjntc.excelexporter.common.constant.ApiEnum;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-03-31 11:59
 */
public class RemoteException extends ResultException{

    public RemoteException(Result<?> result) {
        super(result.setResMsg("Remote call fail.[%s]".formatted(result.getResMsg())));
    }

    public RemoteException(ApiEnum code, String msg) {
        super(Result.buildResult(code,msg));
    }

    public RemoteException(ApiEnum code) {
        this(code, code.getReason());
    }

}
