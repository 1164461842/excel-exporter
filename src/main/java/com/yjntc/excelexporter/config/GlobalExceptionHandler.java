package com.yjntc.excelexporter.config;

import feign.FeignException;

import com.yjntc.excelexporter.ExcelExporterApplication;
import com.yjntc.excelexporter.common.Result;
import com.yjntc.excelexporter.common.constant.ApiEnum;
import com.yjntc.excelexporter.common.constant.ResultBuilder;
import com.yjntc.excelexporter.exception.RemoteException;
import com.yjntc.excelexporter.exception.ResultException;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.slf4j.Logger;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;


/**
 * @author wangkangsheng
 */
@RestControllerAdvice(basePackageClasses = ExcelExporterApplication.class)
public class GlobalExceptionHandler implements ResultBuilder {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<?> result(Exception e){
        e.printStackTrace();
        return error(ApiEnum.RUNTIME_ERROR,e.getMessage());
    }



    @ExceptionHandler(ResultException.class)
    public Result<?> resultException(ResultException e){
        e.printStackTrace();
        return e.getResult();
    }


    @ExceptionHandler(ValidationException.class)
    public Result<?> validationException(ValidationException e){
        e.printStackTrace();
        return error(ApiEnum.RUNTIME_ERROR,e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> validationException(MethodArgumentNotValidException e){
        e.printStackTrace();
        FieldError fieldError = e.getFieldError();
        Result<?> fail = error(ApiEnum.PARAM_ERROR, "请求参数校验未通过");
        if (null != fieldError){
            fail.setResMsg(fieldError.getDefaultMessage());
        }
        return fail;
    }

    @ExceptionHandler(FeignException.class)
    public Result<?> feignException(FeignException e){
        e.printStackTrace();
        throw new RemoteException(error(ApiEnum.REMOTE_EXCEPTION,e.getMessage()));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public Result<?> methodNotAllowedException(MethodNotAllowedException e){
        e.printStackTrace();
        throw new RemoteException(error(ApiEnum.REQUEST_ERROR,"不支持的HTTP方法[%s]".formatted(e.getHttpMethod())));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Result<?> webExchangeBindException(WebExchangeBindException e){
        e.printStackTrace();
        FieldError fieldError = e.getFieldError();
        Result<?> fail = error(ApiEnum.ILLEGAL_PARAM, "请求参数校验未通过");
        if (null != fieldError){
            fail.setResMsg(fieldError.getDefaultMessage());
        }
        return fail;
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public Result<?> webExchangeBindException(FeignException.ServiceUnavailable e){
        e.printStackTrace();
        return error(ApiEnum.REMOTE_EXCEPTION, "远程服务不存在");
    }


}
