package com.cn.xiguaapp.pay.spring.boot.core.configurers;

import com.cn.xiguaapp.pay.spring.boot.core.PayConfigurerAdapter;
import com.cn.xiguaapp.pay.spring.boot.core.builder.InMemoryMerchantDetailsServiceBuilder;
import com.cn.xiguaapp.pay.spring.boot.core.builder.JdbcMerchantDetailsServiceBuilder;
import com.cn.xiguaapp.pay.spring.boot.core.builder.MerchantDetailsServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author xiguaapp
 * @desc 商户列表服务配置
 * @since 1.0 23:32
 */
public class MerchantDetailsServiceConfigurer implements PayConfigurerAdapter<MerchantDetailsServiceBuilder> {

    private MerchantDetailsServiceBuilder builder;

    @Autowired
    private PayMessageConfigurer configurer;

    public void setBuilder(MerchantDetailsServiceBuilder builder) {
        this.builder = builder;
    }

    public InMemoryMerchantDetailsServiceBuilder inMemory() {
        InMemoryMerchantDetailsServiceBuilder builder = MerchantDetailsServiceBuilder.inMemory();
        initBuilder(builder);
        return builder;
    }
    public MerchantDetailsServiceBuilder initBuilder(MerchantDetailsServiceBuilder builder) {
        builder.setConfigurer(this.configurer);
        setBuilder(builder);
        return builder;
    }


    public JdbcMerchantDetailsServiceBuilder jdbc() {
        JdbcMerchantDetailsServiceBuilder builder = MerchantDetailsServiceBuilder.jdbc();
        initBuilder(builder);
        return builder;
    }

    public JdbcMerchantDetailsServiceBuilder jdbc(DataSource source) {
        JdbcMerchantDetailsServiceBuilder builder = MerchantDetailsServiceBuilder.jdbc(source);
        initBuilder(builder);
        return builder;
    }

    /**
     *
     * 将在未来可能进行移除，避免项目不用JdbcTemplate最为数据源操作并且不需要引入JdbcTemplate包时导致的不必要报错
     *
     *  替代方式{@link #jdbc()}与{@link #jdbc(DataSource)}进行替代
     *  <code>
     *      jdbc().template(JdbcTemplate);
     *  </code>
     * @param jdbcTemplate jdbc模版
     * @return jdbc商户列表服务构建器
     */
    @Deprecated
    public JdbcMerchantDetailsServiceBuilder jdbc(JdbcTemplate jdbcTemplate) {
        JdbcMerchantDetailsServiceBuilder builder = MerchantDetailsServiceBuilder.jdbc(jdbcTemplate);
        initBuilder(builder);
        return builder;
    }


    /**
     * 外部调用者使用，链式的做法
     *
     * @return 返回对应外部调用者
     */
    @Override
    public MerchantDetailsServiceBuilder and() {
        return getBuilder();
    }

    /**
     * 获取构建器
     *
     * @return 构建器
     */
    @Override
    public MerchantDetailsServiceBuilder getBuilder() {
        return builder;
    }
}

