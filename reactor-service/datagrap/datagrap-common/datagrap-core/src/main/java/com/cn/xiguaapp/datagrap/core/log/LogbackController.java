package com.cn.xiguaapp.datagrap.core.log;

import com.cn.ykyoung.server.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiguaapp
 * @data 2021-02-21
 * @desc 动态修改日志级别
 */
@RestController
@RequestMapping("/logback")
@Slf4j
public class LogbackController {
    @RequestMapping(value = "/test")
    public String log(){
        log.error("我是error");
        log.warn("我是warn");
        log.info("我是info");
        log.debug("我是debug");
        return "success";
    }


    /**
     * logback动态修改包名的日志级别
     * @param level 日志级别
     * @param packageName 包名
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/level")
    public String updateLogbackLevel(@RequestParam(value="level") String level,
                                            @RequestParam(value="packageName",defaultValue = "-1") String packageName){
        ch.qos.logback.classic.LoggerContext loggerContext =(ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        if(packageName.equals("-1")) {
            // 默认值-1，更改全局日志级别；否则按传递的包名或类名修改日志级别。
            loggerContext.getLogger("root").setLevel(ch.qos.logback.classic.Level.toLevel(level));
        } else {
            try {
                loggerContext.getLogger(packageName).setLevel(ch.qos.logback.classic.Level.valueOf(level));
            }catch (Exception e){
                log.error("【CBR】更改{}日志级别发生错误！！",packageName,e);
                throw new ServiceException("40004","isp.unknown-error");
            }
        }
        return "修改成功";
    }
}
