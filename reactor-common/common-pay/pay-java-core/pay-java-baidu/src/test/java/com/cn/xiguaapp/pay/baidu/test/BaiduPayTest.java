package com.cn.xiguaapp.pay.baidu.test;

import com.cn.xiguaapp.pay.baidu.api.BaiduPayConfigStorage;
import com.cn.xiguaapp.pay.baidu.api.BaiduPayService;
import org.junit.jupiter.api.Test;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 20:55
 */
public class BaiduPayTest {
    @Test
    public void orderInfo() {
        BaiduPayConfigStorage configStorage = new BaiduPayConfigStorage();
        configStorage.setAppid("APP ID");
        configStorage.setAppKey("APP KEY");
        configStorage.setDealId("DEAL ID");
        configStorage.setKeyPublic("KEY PUBLIC");

        BaiduPayService payService = new BaiduPayService(configStorage);
    }
}
