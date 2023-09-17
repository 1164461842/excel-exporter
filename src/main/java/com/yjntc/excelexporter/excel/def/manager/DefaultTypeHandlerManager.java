package com.yjntc.excelexporter.excel.def.manager;


import com.yjntc.excelexporter.excel.ifce.ExcelTypeHandler;
import com.yjntc.excelexporter.excel.ifce.TypeHandlerManager;
import com.yjntc.excelexporter.excel.FieldMapping;
import com.yjntc.excelexporter.excel.def.handler.NullTypeHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-29 16:10
 */
public class DefaultTypeHandlerManager implements TypeHandlerManager {

    /**
     * 处理器缓存
     */
    private static final Map<Class<?>, ExcelTypeHandler<?>> HANDLER_CACHE = new HashMap<>();
    /**
     * 类锁 不干涉其他类的读取 提高并发
     */
    private static final Map<Class<?>, Lock> LOCK_MAP = new ConcurrentHashMap<>(2);

    /**
     * 处理器列表
     */
    private static final List<ExcelTypeHandler<?>> HANDLER_CONTEXT = new ArrayList<>(10);

    /**
     * 空值处理器
     */
    private static final ExcelTypeHandler<Object> NULL_TYPE_HANDLER = new NullTypeHandler();

    /**
     * 获得处理器
     *
     * @param o
     *         Object 对象
     * @param fm
     *         FieldMapping 字段信息
     *
     * @return TypeHandler<?>
     */
    @Override
    public ExcelTypeHandler<?> getHandler(Object o, FieldMapping fm) {
        if (null == o) {
            return NULL_TYPE_HANDLER;
        }
        Class<?> clazz = o.getClass();
        ExcelTypeHandler<?> typeHandler = HANDLER_CACHE.get(clazz);
        if (null == typeHandler){
            Lock lock = LOCK_MAP.computeIfAbsent(clazz, k -> new ReentrantLock());
            lock.lock();
            try {
                typeHandler = HANDLER_CACHE.get(clazz);
                if (null == typeHandler){
                    for (ExcelTypeHandler<?> handler : HANDLER_CONTEXT) {
                        Class<?> type = handler.getType();
                        if (type.isAssignableFrom(clazz)){
                            typeHandler = handler;
                            break;
                        }
                    }
                }else {
                    HANDLER_CACHE.put(clazz,typeHandler);
                }
            }finally {
                lock.unlock();
            }
        }
        return typeHandler;
    }

    /**
     * 注册类型处理器
     *
     * @param eth
     *         ExcelTypeHandler<?> 类型处理器
     */
    @Override
    public void register(ExcelTypeHandler<?> eth) {
        HANDLER_CONTEXT.add(eth);
    }

}
