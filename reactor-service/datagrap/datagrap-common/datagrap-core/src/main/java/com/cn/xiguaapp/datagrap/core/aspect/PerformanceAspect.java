package com.cn.xiguaapp.datagrap.core.aspect;

import com.cn.xiguaapp.datagrap.core.aspect.annotation.InvokeTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author xiguaapp
 * @data 2021-02-21
 * @desc 性能监控AOP
 */
@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    /**
     * 性能监控监测
     * @param pjp
     * @param invokeTime
     * @return
     * @throws Throwable
     */
    @Around("@annotation(invokeTime)")
    public Object beforeSwitchDS(ProceedingJoinPoint pjp, InvokeTime invokeTime) throws Throwable{
        long begin = System.currentTimeMillis();
        String method = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().getName();
        Object ret = pjp.proceed();
        long l = System.currentTimeMillis() - begin;
        if(log.isDebugEnabled()){
            log.debug("【性能监控】 method<{}.{}> cost time <{}>ms",className,method, l);
        }else{
            if(l>100){
                log.info("【性能监控】 method<{}.{}> cost time <{}>ms",className,method, l);
            }
        }
        return ret;
    }
}
