package com.yjntc.excelexporter.common;




import com.yjntc.excelexporter.common.constant.ApiEnum;

import java.io.Serializable;

public class Result<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer resCode;

    /**
     * 获取状态。
     *
     * @return 状态
     */
    public Integer getResCode() {
        return resCode;
    }

    /**
     * 状态信息,错误描述.
     */
    private String resMsg;

    /**
     * 获取消息内容。
     *
     * @return 消息
     */
    public String getResMsg() {
        return resMsg;
    }

    /**
     * 数据.
     */
    private T resObj;

    /**
     * 获取数据内容。
     *
     * @return 数据
     */
    public T getResObj() {
        return resObj;
    }

    public Result(Integer resCode, String resMsg, T resObj) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.resObj = resObj;
    }

    public Result(Integer resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public Result(String resMsg) {
        this.resMsg = resMsg;
    }

    public Result() {
    }

    /**
     * 创建一个带有状态、消息和数据的结果对象.
     *
     * @param resCode  状态
     * @param resMsg 消息内容
     * @param resObj    数据
     * @return 结构数据
     */
    public static <T> Result<T> buildResult(ApiEnum resCode, String resMsg, T resObj) {
        return new Result<>(resCode.getCode(), resMsg, resObj);
    }

    /**
     * 创建一个带有状态、消息和数据的结果对象.
     *
     * @param resCode  状态
     * @param resMsg 消息内容
     * @return 结构数据
     */
    public static <T> Result<T> buildResult(ApiEnum resCode, String resMsg) {
        return new Result<>(resCode.getCode(), resMsg);
    }

    public static <T> Result<T> buildResult(int resCode, String resMsg) {
        return new Result<>(resCode, resMsg);
    }

    public static <T> Result<T> buildResult(int resCode, String resMsg,T data) {
        return new Result<>(resCode, resMsg,data);
    }

    /**
     * 创建一个带有状态和数据的结果对象.
     *
     * @param resCode 状态
     * @param resObj   数据
     * @return 结构数据
     */
    public static <T> Result<T> buildResult(ApiEnum resCode, T resObj) {
        return new Result<>(resCode.getCode(), resCode.getReason(), resObj);
    }

    /**
     * 创建一个带有状态的结果对象.
     *
     * @param resCode 状态
     * @return 结构数据
     */
    public static <T> Result<T> buildResult(ApiEnum resCode) {
        return new Result<>(resCode.getCode(), resCode.getReason());
    }

    public Result<T> setResCode(Integer resCode) {
        this.resCode = resCode;
        return this;
    }

    public Result<T> setResMsg(String resMsg) {
        this.resMsg = resMsg;
        return this;
    }

    public Result<T> setResObj(T resObj) {
        this.resObj = resObj;
        return this;
    }
}

