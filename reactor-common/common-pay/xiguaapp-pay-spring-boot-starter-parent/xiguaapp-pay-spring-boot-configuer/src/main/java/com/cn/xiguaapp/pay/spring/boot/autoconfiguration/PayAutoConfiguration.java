package com.cn.xiguaapp.pay.spring.boot.autoconfiguration;

import com.cn.xiguaapp.pay.spring.boot.core.MerchantPayServiceManager;
import com.cn.xiguaapp.pay.spring.boot.core.PayServiceConfigurer;
import com.cn.xiguaapp.pay.spring.boot.core.PayServiceManager;
import com.cn.xiguaapp.pay.spring.boot.core.configurers.DefalutPayMessageConfigurer;
import com.cn.xiguaapp.pay.spring.boot.core.configurers.MerchantDetailsServiceConfigurer;
import com.cn.xiguaapp.pay.spring.boot.core.configurers.PayMessageConfigurer;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.MerchantDetailsService;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatform;
import com.cn.xiguaapp.pay.spring.boot.core.plateform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @author xiguaapp
 * @desc 支付转载配置
 * @since 1.0 22:38
 */
@Configuration
@ImportAutoConfiguration({AliPaymentPlatform.class, FuiouPaymentPlatform.class, PayoneerPaymentPlatform.class, PaypalPaymentPlatform.class, UnionPaymentPlatform.class, WxPaymentPlatform.class, YoudianPaymentPlatform.class})
public class PayAutoConfiguration {
    @Autowired
    @Order
    public void loadPaymentPlatforms(List<PaymentPlatform> platforms){
        for (PaymentPlatform platform : platforms){
            PaymentPlatforms.loadPaymentPlatform(platform);
        }
    }


    @Bean
    @ConditionalOnMissingBean(MerchantDetailsServiceConfigurer.class)
    public MerchantDetailsServiceConfigurer detailsServiceConfigurer(){
        return new MerchantDetailsServiceConfigurer();
    }


    @Bean
    @ConditionalOnMissingBean(PayServiceManager.class)
    public MerchantPayServiceManager payServiceManager(){
        return new MerchantPayServiceManager();
    }


    @Bean
    @ConditionalOnMissingBean(PayMessageConfigurer.class)
    public PayMessageConfigurer messageHandlerConfigurer(){
        return new DefalutPayMessageConfigurer();
    }

    @Bean
    @ConditionalOnMissingBean(MerchantDetailsService.class)
    @ConditionalOnBean(PayServiceConfigurer.class)
    protected MerchantDetailsService configure(PayServiceConfigurer configurer, MerchantDetailsServiceConfigurer merchantDetails, PayMessageConfigurer payMessageConfigurer) {
        configurer.configure(merchantDetails);
        configurer.configure(payMessageConfigurer);
        MerchantDetailsService detailsService = merchantDetails.getBuilder().build();
        return detailsService;
    }

}
