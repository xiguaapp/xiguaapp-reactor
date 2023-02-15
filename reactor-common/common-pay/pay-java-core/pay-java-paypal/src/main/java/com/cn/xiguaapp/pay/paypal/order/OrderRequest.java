package com.cn.xiguaapp.pay.paypal.order;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 21:26
 */
public class OrderRequest {
    @JSONField(name = "application_context")
    private ApplicationContext applicationContext;
    @JSONField(name = "intent")
    private String checkoutPaymentIntent;

    @JSONField(name =
            "purchase_units"
    )
    private List<PurchaseUnitRequest> purchaseUnits;


    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getCheckoutPaymentIntent() {
        return checkoutPaymentIntent;
    }

    public void setCheckoutPaymentIntent(String checkoutPaymentIntent) {
        this.checkoutPaymentIntent = checkoutPaymentIntent;
    }


    public List<PurchaseUnitRequest> getPurchaseUnits() {
        return purchaseUnits;
    }

    public void setPurchaseUnits(List<PurchaseUnitRequest> purchaseUnits) {
        this.purchaseUnits = purchaseUnits;
    }
}
