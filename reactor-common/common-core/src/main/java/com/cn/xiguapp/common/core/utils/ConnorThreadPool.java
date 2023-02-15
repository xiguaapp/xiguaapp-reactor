package com.cn.xiguapp.common.core.utils;

import java.util.concurrent.*;

/**
 * @Date 2020/4/23 9:38
 * @desctipt: 自定义线程池
 * @Author 冰柚009
 */
public class ConnorThreadPool {
    //默认为io密集型
    public static ExecutorService newThreadExecutors(){
        return newIoThreadExecutors();
    }

    /**
     * 根据bool类型判断使用哪一种线程池
     * @param fair
     * @return
     */
    public static ExecutorService newThreadExecutors(boolean fair){
        return fair?newIoThreadExecutors():newCPUThreadExcutors();
    }
    //获取CPU核心数
    private static int adaptableProcessors(){
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * IO密集型型(CPU在等读写操作 空闲时间多) 线程池
     * @return
     */
    private static ExecutorService newIoThreadExecutors(){
        return threadPool(adaptableProcessors()*2);
    }

    /**
     * cpu密集型（cpu利用率及时间超过io知性的时间）
     * @return
     */
    private static ExecutorService newCPUThreadExcutors(){
        return threadPool(adaptableProcessors()+1);
    }

    /**
     *
     * @param processours
     * @return
     */
    private static ExecutorService threadPool(int processours){
        return new ThreadPoolExecutor(0,processours,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(20),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }
}
