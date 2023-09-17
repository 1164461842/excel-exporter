package com.yjntc.excelexporter;

import cn.hutool.core.thread.ThreadUtil;
import com.yjntc.excelexporter.command.RandomPortCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableDiscoveryClient
public class ExcelExporterApplication {

    public static void main(String[] args) {
        RandomPortCommand randomPortCommand = new RandomPortCommand();
        int port = randomPortCommand.startPortRandomCommand(args);
        SpringApplication.run(ExcelExporterApplication.class, args);
        ThreadUtil.execAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Applicaiton working on port ["+port+"]");
        });
    }

}
