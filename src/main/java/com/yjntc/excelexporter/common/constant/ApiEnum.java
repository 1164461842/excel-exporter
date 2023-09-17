package com.yjntc.excelexporter.common.constant;

public enum ApiEnum {

    /**
     * 状态
     */
    SUCCESS(0,"SUCCESS"),
    /**
     * 程序运行异常
     */
    RUNTIME_ERROR(1120001,"程序运行异常"),
    /***
     * 未知异常
     */
    UN_KNOW_ERROR(1120002,"未知异常"),
    /**
     * 非法参数
     */
    ILLEGAL_PARAM(1120003,"参数缺失"),
    /**
     * 参数异常
     */
    PARAM_ERROR(1120004,"参数异常"),
    /**
     * 缺少Token信息
     */
    MISS_TOKEN_ERROR(1120005,"缺少Token信息"),
    /**
     * 缺少角色信息
     */
    MISS_ROLE_ERROR(1120006,"缺少角色信息"),
    /**
     * Token验证不通过
     */
    INVALID_TOKEN_ERROR(1120007,"Token验证不通过"),
    /**
     * 远程调用异常
     */
    REMOTE_EXCEPTION(1121010,"远程调用异常"),

    /**
     * 不支持的操作
     */
    UN_SUPPORT_OPERATE(1121020,"不支持的操作"),

    /**
     * 请求异常
     */
    REQUEST_ERROR(1121030,"请求异常"),

    /**
     * 角色校验未通过
     */
    ROLE_NOT_ALLOW(1128889,"角色校验未通过"),

    E_1129999(1129999,"未知异常");


    /**
     * 状态码,长度固定为7位的字符串.
     *  前缀为统一分配的3位
     */
    private int code;

    /**
     * 错误信息.
     */
    private String reason;

    ApiEnum(Integer code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public Integer getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return code + ": " + reason;
    }


    public final static String CODE="resCode";
    public final static String OBJ="resObj";
    public final static String MSG="resMsg";

}

