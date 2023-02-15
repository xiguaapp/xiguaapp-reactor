package com.cn.xiguaapp.pay.spring.boot.core.builder;

import com.cn.xiguaapp.pay.spring.boot.core.merchant.MerchantDetailsService;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.PaymentPlatformMerchantDetails;
import com.cn.xiguaapp.pay.spring.boot.core.merchant.info.*;
import com.cn.xiguaapp.pay.spring.boot.core.provider.InMemoryMerchantDetailsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiguaapp
 * @desc 内存型商户列表服务构建器
 * @since 1.0 23:29
 */
public class InMemoryMerchantDetailsServiceBuilder extends MerchantDetailsServiceBuilder {

    private List<PaymentPlatformMerchantDetails> merchantDetails = new ArrayList<PaymentPlatformMerchantDetails>();



    public void addMerchantDetails(PaymentPlatformMerchantDetails merchantDetail) {
        this.merchantDetails.add(merchantDetail);
    }

    public AliMerchantDetails ali(){
        AliMerchantDetails details = new AliMerchantDetails(this);
        addMerchantDetails(details);
        return details;
    }

    public FuiouMerchantDetails fuiou(){
        FuiouMerchantDetails details = new FuiouMerchantDetails(this);
        addMerchantDetails(details);
        return details;
    }
    public PayoneerMerchantDetails payoneer(){
        PayoneerMerchantDetails details = new PayoneerMerchantDetails(this);
        addMerchantDetails(details);
        return details;
    }
    public PaypalMerchantDetails payPal(){
        PaypalMerchantDetails details = new PaypalMerchantDetails(this);
        addMerchantDetails(details);
        return details;
    }
    public UnionMerchantDetails union(){
        UnionMerchantDetails details = new UnionMerchantDetails(this);
        addMerchantDetails(details);
        return details;
    }
    public WxMerchantDetails wx(){
        WxMerchantDetails details = new WxMerchantDetails(this);
        addMerchantDetails(details);
        return details;
    }


    /**
     * 开始构建
     *
     * @return 商户列表服务
     */
    @Override
    protected MerchantDetailsService performBuild() {
        InMemoryMerchantDetailsManager merchantDetailsManager = new InMemoryMerchantDetailsManager();
        merchantDetailsManager.setPayMessageConfigurer(configurer);
        merchantDetailsManager.createMerchant(merchantDetails);
        return merchantDetailsManager;
    }
}
