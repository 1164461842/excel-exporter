package com.yjntc.excelexporter.excel;

import com.yjntc.excelexporter.excel.def.DynamicFieldDataGetter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 *  动态javassist可用检测
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-05-03 16:47
 */
@Component
public class RunnerChecker implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean canUse = DynamicFieldDataGetter.canUse();
        if (!canUse){
            throw new IllegalArgumentException("请将参数["+DynamicFieldDataGetter.VM_OPTIONS+"]添加到VM启动参数中,以开启javassist反射优化.");
        }
    }

}
