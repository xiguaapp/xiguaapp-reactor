package com.cn.xiguaapp.pay.spring.boot.core.builder;

import com.cn.xiguaapp.pay.spring.boot.core.merchant.MerchantDetailsService;
import com.cn.xiguaapp.pay.spring.boot.core.provider.CacheMerchantDetailsManager;
import com.cn.xiguaapp.pay.spring.boot.core.provider.JdbcMerchantDetailsManager;
import com.cn.xiguaapp.pay.spring.boot.core.provider.MerchantDetailsManager;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author xiguaapp
 * @desc 内存型商户列表服务构建器
 * @since 1.0 23:28
 */
public class JdbcMerchantDetailsServiceBuilder extends MerchantDetailsServiceBuilder {

    private JdbcTemplate jdbcTemplate;

    private boolean cache = false;


    public JdbcMerchantDetailsServiceBuilder(DataSource source) {
        setJdbcTemplate(new JdbcTemplate(source));
    }

    public JdbcMerchantDetailsServiceBuilder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcMerchantDetailsServiceBuilder(boolean cache) {
        this.cache = cache;
    }

    public JdbcMerchantDetailsServiceBuilder() {
    }


    /**
     * 设置缓存
     *
     * @param cache 缓存
     * @return 当前
     */
    public JdbcMerchantDetailsServiceBuilder cache(boolean cache) {
        setCache(cache);
        return this;
    }

    /**
     * 设置jdbc 模版
     *
     * @param jdbcTemplate jdbcTemplate
     * @return 当前
     */
    public JdbcMerchantDetailsServiceBuilder template(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
        return this;
    }


    /**
     * 设置数据源
     *
     * @param source 数据源
     * @return 当前
     */
    public JdbcMerchantDetailsServiceBuilder dataSource(DataSource source) {
        setJdbcTemplate(new JdbcTemplate(source));
        return this;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        if (null != this.jdbcTemplate) {
            return;
        }
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    /**
     * 开始构建
     *
     * @return 商户列表服务·
     */
    @Override
    protected MerchantDetailsService performBuild() {

        MerchantDetailsManager manager = new JdbcMerchantDetailsManager(jdbcTemplate);
        if (cache) {
            manager = new CacheMerchantDetailsManager(manager);
        }
        manager.setPayMessageConfigurer(configurer);

        return manager;
    }
}
