package com.cn.xiguaapp.common.oauth.core.service.impl;

import com.cn.xiguaapp.common.oauth.core.manage.SaTokenManager;
import com.cn.xiguaapp.common.oauth.core.service.SaTokenService;
import com.cn.xiguaapp.common.oauth.core.utils.SaTaskUtil;
import com.cn.xiguaapp.common.oauth.core.utils.SaTokenInsideUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 * @desc 持久层默认的实现类 , 基于内存Map
 * @since
 */
public class SaTokenServiceDefaultImpl implements SaTokenService {
    /**
     * 数据集合 
     */
    public Map<String, Object> dataMap = new ConcurrentHashMap<String, Object>();

    /**
     * 过期时间集合 (单位: 毫秒) , 记录所有key的到期时间 [注意不是剩余存活时间] 
     */
    public Map<String, Long> expireMap = new ConcurrentHashMap<String, Long>();

    /**
     * 构造函数
     */
    public SaTokenServiceDefaultImpl() {
        initRefreshTimer();
    }


    // ------------------------ String 读写操作 

    @Override
    public String get(String key) {
        clearKeyByTimeout(key);
        return (String)dataMap.get(key);
    }

    @Override
    public void set(String key, String value, long timeout) {
        dataMap.put(key, value);
        expireMap.put(key, (timeout == SaTokenService.NEVER_EXPIRE) ? (SaTokenService.NEVER_EXPIRE) : (System.currentTimeMillis() + timeout * 1000));
    }

    @Override
    public void update(String key, String value) {
        if(getKeyTimeout(key) == SaTokenService.NOT_VALUE_EXPIRE) {
            return;
        }
        dataMap.put(key, value);
    }

    @Override
    public void delete(String key) {
        dataMap.remove(key);
        expireMap.remove(key);
    }

    @Override
    public long getTimeout(String key) {
        return getKeyTimeout(key);
    }

    @Override
    public void updateTimeout(String key, long timeout) {
        expireMap.put(key, System.currentTimeMillis() + timeout * 1000);
    }


    // ------------------------ Object 读写操作 

    @Override
    public Object getObject(String key) {
        clearKeyByTimeout(key);
        return dataMap.get(key);
    }

    @Override
    public void setObject(String key, Object object, long timeout) {
        dataMap.put(key, object);
        expireMap.put(key, (timeout == SaTokenService.NEVER_EXPIRE) ? (SaTokenService.NEVER_EXPIRE) : (System.currentTimeMillis() + timeout * 1000));
    }

    @Override
    public void updateObject(String key, Object object) {
        if(getKeyTimeout(key) == SaTokenService.NOT_VALUE_EXPIRE) {
            return;
        }
        // 无动作 
    }

    @Override
    public void deleteObject(String key) {
        dataMap.remove(key);
        expireMap.remove(key);
    }

    @Override
    public long getObjectTimeout(String key) {
        return getKeyTimeout(key);
    }

    @Override
    public void updateObjectTimeout(String key, long timeout) {
        expireMap.put(key, System.currentTimeMillis() + timeout * 1000);
    }


    // ------------------------ Session 读写操作 
    // 使用接口默认实现 


    // ------------------------ 过期时间相关操作 

    /**
     * 如果指定key已经过期，则立即清除它 
     * @param key 指定key 
     */
    void clearKeyByTimeout(String key) {
        Long expirationTime = expireMap.get(key);
        // 清除条件：如果不为空 && 不是[永不过期] && 已经超过过期时间 
        if(expirationTime != null && !expirationTime.equals(SaTokenService.NEVER_EXPIRE) && expirationTime < System.currentTimeMillis()) {
            dataMap.remove(key);
            expireMap.remove(key);
        }
    }

    /**
     * 获取指定key的剩余存活时间 (单位：秒)
     */
    long getKeyTimeout(String key) {
        // 先检查是否已经过期
        clearKeyByTimeout(key);
        // 获取过期时间 
        Long expire = expireMap.get(key);
        // 如果根本没有这个值 
        if(expire == null) {
            return SaTokenService.NOT_VALUE_EXPIRE;
        }
        // 如果被标注为永不过期 
        if(expire.equals(SaTokenService.NEVER_EXPIRE)) {
            return SaTokenService.NEVER_EXPIRE;
        }
        // ---- 计算剩余时间并返回 
        long timeout = (expire - System.currentTimeMillis()) / 1000;
        // 小于零时，视为不存在 
        if(timeout < 0) {
            dataMap.remove(key);
            expireMap.remove(key);
            return SaTokenService.NOT_VALUE_EXPIRE;
        }
        return timeout;
    }


    // --------------------- 定时清理过期数据  

    /**
     * 定时任务对象 
     */
    public Timer refreshTimer;

    /**
     * 清理所有已经过期的key 
     */
    public void refreshDataMap() {
        Iterator<String> keys = expireMap.keySet().iterator();
        while (keys.hasNext()) {
            clearKeyByTimeout(keys.next());
        }
    }

    /**
     * 初始化定时任务 
     */
    public void initRefreshTimer() {
        // 如果已经被初始化过了, 则停止它
        if(this.refreshTimer != null) {
            this.endRefreshTimer();
        }

        // 开始新的定时任务
        if(SaTokenManager.getConfig().getDataRefreshPeriod() < 0) {
            return;
        }
        int period = SaTokenManager.getConfig().getDataRefreshPeriod() * 1000;
        this.refreshTimer = SaTaskUtil.setInterval(new SaTaskUtil.FunctionRunClass() {
            @Override
            public void run() {
                refreshDataMap();
            }
        }, period, period);
    }

    /**
     * 结束定时任务
     */
    public void endRefreshTimer() {
        this.refreshTimer.cancel();
    }




    // --------------------- 会话管理 

    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size) {
        return SaTokenInsideUtil.searchList(expireMap.keySet(), prefix, keyword, start, size);
    }
}
