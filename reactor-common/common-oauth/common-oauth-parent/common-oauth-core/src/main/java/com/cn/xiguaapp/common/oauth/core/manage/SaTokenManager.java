package com.cn.xiguaapp.common.oauth.core.manage;

import com.cn.xiguaapp.common.oauth.core.action.SaTokenAction;
import com.cn.xiguaapp.common.oauth.core.action.SaTokenActionDefaultImpl;
import com.cn.xiguaapp.common.oauth.core.config.SaTokenConfig;
import com.cn.xiguaapp.common.oauth.core.config.SaTokenConfigFactory;
import com.cn.xiguaapp.common.oauth.core.cookie.SaTokenCookie;
import com.cn.xiguaapp.common.oauth.core.cookie.SaTokenCookieDefaultImpl;
import com.cn.xiguaapp.common.oauth.core.core.StpInterface;
import com.cn.xiguaapp.common.oauth.core.core.StpInterfaceDefaultImpl;
import com.cn.xiguaapp.common.oauth.core.service.SaTokenService;
import com.cn.xiguaapp.common.oauth.core.service.impl.SaTokenServiceDefaultImpl;
import com.cn.xiguaapp.common.oauth.core.servlet.SaTokenServlet;
import com.cn.xiguaapp.common.oauth.core.servlet.SaTokenServletDefaultImpl;
import com.cn.xiguaapp.common.oauth.core.utils.SaTokenInsideUtil;

/**
 * @author xiguaapp
 * @desc 管理所有接口对象
 * @since
 */
public class SaTokenManager {


    /**
     * 配置文件 Bean
     */
    private static SaTokenConfig config;
    public static SaTokenConfig getConfig() {
        if (config == null) {
            initConfig();
        }
        return config;
    }
    public static void setConfig(SaTokenConfig config) {
        SaTokenManager.config = config;
        if(config.getIsV()) {
            SaTokenInsideUtil.printSaToken();
        }
    }
    public synchronized static void initConfig() {
        if (config == null) {
            setConfig(SaTokenConfigFactory.createConfig());
        }
    }

    /**
     * 持久化 Bean
     */
    public static SaTokenService saTokenDao;
    public static SaTokenService getSaTokenDao() {
        if (saTokenDao == null) {
            initSaTokenDao();
        }
        return saTokenDao;
    }
    public static void setSaTokenDao(SaTokenService saTokenDao) {
        if(SaTokenManager.saTokenDao != null && (SaTokenManager.saTokenDao instanceof SaTokenServiceDefaultImpl)) {
            ((SaTokenServiceDefaultImpl)SaTokenManager.saTokenDao).endRefreshTimer();
        }
        SaTokenManager.saTokenDao = saTokenDao;
    }
    public synchronized static void initSaTokenDao() {
        if (saTokenDao == null) {
            setSaTokenDao(new SaTokenServiceDefaultImpl());
        }
    }

    /**
     * 权限认证 Bean
     */
    public static StpInterface stpInterface;
    public static StpInterface getStpInterface() {
        if (stpInterface == null) {
            initStpInterface();
        }
        return stpInterface;
    }
    public static void setStpInterface(StpInterface stpInterface) {
        SaTokenManager.stpInterface = stpInterface;
    }
    public synchronized static void initStpInterface() {
        if (stpInterface == null) {
            setStpInterface(new StpInterfaceDefaultImpl());
        }
    }

    /**
     * 框架行为 Bean
     */
    public static SaTokenAction saTokenAction;
    public static SaTokenAction getSaTokenAction() {
        if (saTokenAction == null) {
            initSaTokenAction();
        }
        return saTokenAction;
    }
    public static void setSaTokenAction(SaTokenAction saTokenAction) {
        SaTokenManager.saTokenAction = saTokenAction;
    }
    public synchronized static void initSaTokenAction() {
        if (saTokenAction == null) {
            setSaTokenAction(new SaTokenActionDefaultImpl());
        }
    }

    /**
     * Cookie操作 Bean
     */
    public static SaTokenCookie saTokenCookie;
    public static SaTokenCookie getSaTokenCookie() {
        if (saTokenCookie == null) {
            initSaTokenCookie();
        }
        return saTokenCookie;
    }
    public static void setSaTokenCookie(SaTokenCookie saTokenCookie) {
        SaTokenManager.saTokenCookie = saTokenCookie;
    }
    public synchronized static void initSaTokenCookie() {
        if (saTokenCookie == null) {
            setSaTokenCookie(new SaTokenCookieDefaultImpl());
        }
    }

    /**
     * Servlet操作 Bean
     */
    public static SaTokenServlet saTokenServlet;
    public static SaTokenServlet getSaTokenServlet() {
        if (saTokenServlet == null) {
            initSaTokenServlet();
        }
        return saTokenServlet;
    }
    public static void setSaTokenServlet(SaTokenServlet saTokenServlet) {
        SaTokenManager.saTokenServlet = saTokenServlet;
    }
    public synchronized static void initSaTokenServlet() {
        if (saTokenServlet == null) {
            setSaTokenServlet(new SaTokenServletDefaultImpl());
        }
    }
}
