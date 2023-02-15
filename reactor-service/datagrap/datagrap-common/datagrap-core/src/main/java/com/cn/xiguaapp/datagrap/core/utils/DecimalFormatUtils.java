package com.cn.xiguaapp.datagrap.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author xiguaapp
 * @date 2021-02-21
 * @desc 数字转换增长率类
 */
@Slf4j
public class DecimalFormatUtils {
    private static final DecimalFormat df = new DecimalFormat("#.00");
    private static NumberFormat nf;
    static {
        nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
    }

    public static BigDecimal getPointBack2(Double t) throws NumberFormatException{
        String format = nf.format(t);
        return new BigDecimal(format);

    }
    public static BigDecimal getPointBack2(Float t) throws NumberFormatException{
        String format = df.format(t);
        return new BigDecimal(format);
    }
    //计算增长率
    public static BigDecimal getRate(Double t,Double t2){
        double v= (t - t2) / t2*100;
        try {
            String format = nf.format(v);
            return new BigDecimal(format);
        }catch (NumberFormatException e){
            log.error("【计算增长率】数字1:{},数字1:{}，增长率:{}",t,t2,v,e);
        }
        return BigDecimal.ZERO;
    }
}
