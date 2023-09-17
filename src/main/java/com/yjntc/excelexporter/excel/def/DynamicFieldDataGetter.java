package com.yjntc.excelexporter.excel.def;

import com.yjntc.excelexporter.excel.ifce.FieldDataGetter;
import javassist.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  动态字段读取器
 *   使用字节码动态编译技术 弥补反射的效率低下问题,可大幅度提高反射效率
 *   切记 启动参数需要添加 --add-opens java.base/java.lang=ALL-UNNAMED
 *   <b>--add-opens java.base/java.lang=ALL-UNNAMED<b/>
 * @author WangKangSheng
 * @date 2022-04-28 18:34
 */
public class DynamicFieldDataGetter implements FieldDataGetter<Object> {

    /**
     * 生成器缓存
     */
    private static final Map<Class<?>, FieldDataGetter<Object>> FDG_CACHE = new HashMap<>();
    /**
     * 类锁 不干涉其他类的读取 提高并发
     */
    private static final Map<Class<?>, Lock> LOCK_MAP = new ConcurrentHashMap<>(2);

    /**
     * 生成的类前缀
     */
    private static final String CLASS_NAME_PREFIX = DynamicFieldDataGetter.class.getSimpleName();

    public static final String VM_OPTIONS = "--add-opens java.base/java.lang=ALL-UNNAMED";

    @Override public Object getFromBean(Object bean, String field) {
        if (null == bean){
            throw new NullPointerException();
        }
        Class<?> tls = bean.getClass();
        FieldDataGetter<Object> objectFieldDataGetter = FDG_CACHE.get(tls);
        if (null == objectFieldDataGetter){
            Lock lock = LOCK_MAP.computeIfAbsent(tls, k -> new ReentrantLock());
            lock.lock();
            try {
                objectFieldDataGetter = FDG_CACHE.get(tls);
                if (null == objectFieldDataGetter){
                    try {
                        objectFieldDataGetter = generateGetter(tls);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    FDG_CACHE.put(tls,objectFieldDataGetter);
                }
                return objectFieldDataGetter.getFromBean(bean, field);
            }finally {
                lock.unlock();
            }
        }
        return objectFieldDataGetter.getFromBean(bean, field);
    }


    /**
     * 创建getter器
     *  兼容给如的class类型 使用字节码生成技术可大幅的提高反射效率
     *  调试模式可打开方法中的注释  查看生成的字节码内容
     * @param tls Class<?>
     * @return FieldDataGetter<Object>
     * @throws NotFoundException e
     * @throws CannotCompileException e
     * @throws NoSuchMethodException e
     * @throws InvocationTargetException e
     * @throws InstantiationException e
     * @throws IllegalAccessException e
     */
    private static FieldDataGetter<Object> generateGetter(Class<?> tls) throws NotFoundException,
                                                                               CannotCompileException,
                                                                               NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ClassPool classPool = ClassPool.getDefault();

        CtClass ctClass = classPool.makeClass(CLASS_NAME_PREFIX + "&" + tls.getSimpleName());

        CtClass fdgCtClassIfe = classPool.getCtClass(FieldDataGetter.class.getTypeName());
        ctClass.addInterface(fdgCtClassIfe);

        String methodBody = buildBodyMethod(tls);
        CtMethod ctMethod = CtNewMethod.make(methodBody, ctClass);

        ctClass.addMethod(ctMethod);

        // debug model
        // debugFile(ctClass);

        Class<?> aClass = ctClass.toClass();
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();

        return (FieldDataGetter<Object>) declaredConstructor.newInstance();
    }

    private static void debugFile(CtClass ctClass){
        try {
            ctClass.writeFile("./tmp_getter");
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成类的方法体
     * @param tls Class<?> 类对象
     * @return String 方法体字符串
     */
    private static String buildBodyMethod(Class<?> tls){

        StringBuilder sb = new StringBuilder()
                .append("public Object getFromBean(")
                .append("Object bean, String field) {")
                .append("\n")
                .append("\n");
        sb.append(tls.getName())
                .append(" t;\n")
                .append("if(bean instanceof "+tls.getName()+" ){\n\t t = ("+tls.getName()+") bean; \n}")
                .append("else { \n\tSystem.err.println(\"不兼容的实体类[\"+bean.getClass().getName()+\"] >> ["+tls.getName()+"]\"); \n return \"\";}\n\n")
                ;
        Field[] declaredFields = tls.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            String getterMethod = "get"+fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);

            try {
                tls.getDeclaredMethod(getterMethod);
            }catch (Exception e){
                System.err.println("没有找到["+fieldName+"]的get方法["+getterMethod+"]");
                continue;
            }
            sb
                .append("\tif(\"")
                .append(fieldName)
                .append("\".equals(field)){ return t.")
                .append(getterMethod)
                .append("();}\n")
            ;
        }
        sb
            .append("\n\tSystem.err.println(\"没有字段[\"+field+\"]的getter方法)\");")
            .append("\n");
        return sb
                .append("return \"\";\n}")
                .toString();
    }

    public static boolean canUse(){
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = bean.getInputArguments();
        return null != arguments && !arguments.isEmpty() && arguments.contains("--add-opens=java.base/java.lang=ALL-UNNAMED");
    }


}
