package com.yjntc.excelexporter.command;

import cn.hutool.core.net.NetUtil;
import org.springframework.util.StringUtils;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-03-29 15:36
 */
public class RandomPortCommand {
    public int startPortRandomCommand(String[] args) {
        boolean isServerPort = false;
        String serverPort = "";
        if (args != null) {
            for (String arg : args) {
                if(StringUtils.hasText(arg)&&arg.startsWith("--server.port")){
                    isServerPort=true;
                    serverPort=arg;
                    break;
                }
            }
        }
        //没有指定端口，则随机生成一个可用端口
        if(!isServerPort){
            int port = NetUtil.getUsableLocalPort(10_000,50_000);
            System.setProperty("server.port",String.valueOf(port));
            return port;
        }else{
            System.setProperty("server.port",serverPort.split("=")[1]);
            return Integer.parseInt(serverPort.split("=")[1]);
        }
    }
}
