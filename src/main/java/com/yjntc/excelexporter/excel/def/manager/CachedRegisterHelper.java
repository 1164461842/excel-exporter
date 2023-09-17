package com.yjntc.excelexporter.excel.def.manager;

import com.yjntc.excelexporter.excel.def.handler.DateTypeHandler;
import com.yjntc.excelexporter.excel.ifce.ExcelTypeHandler;
import com.yjntc.excelexporter.excel.ifce.TypeHandlerManager;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-29 16:43
 */
public class CachedRegisterHelper {

    /**
     * 处理器列表
     */
    private static final Set<ExcelTypeHandler<?>> HANDLER_CONTEXT = new HashSet<>(10);
    /**
     * 默认注册器
     */
    private static final TypeHandlerManager DEF_MANAGER = new DefaultTypeHandlerManager();
    /**
     * 是否已经注册
     */
    private static boolean registered = false;


    /**
     * 注册类型处理器
     *
     * @param eth
     *         ExcelTypeHandler<?> 类型处理器
     */
    public static void register(ExcelTypeHandler<?> eth) {
        HANDLER_CONTEXT.add(eth);
    }

    public static TypeHandlerManager getDefManager(){
        init();
        if (!registered){
            pushRegister(DEF_MANAGER);
            registered = true;
        }
        return DEF_MANAGER;
    }

    public static void pushRegister(TypeHandlerManager manager){
        for (ExcelTypeHandler<?> excelTypeHandler : HANDLER_CONTEXT) {
            manager.register(excelTypeHandler);
        }
    }

    private static void init(){
        register(new DateTypeHandler());
    }

}
