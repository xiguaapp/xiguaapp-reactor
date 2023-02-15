package com.cn.xiguaapp.common.gateway.validate;

import com.cn.xiguaapp.common.gateway.bean.GatewayConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author xiguaapp
 */
public class SignConfig {
    private static volatile Wrapper wrapper = new Wrapper() {};

    public static void enableUrlencodeMode() {
        wrapper = new Wrapper() {
            @Override
            public String wrapVal(Object val) {
                String valStr = String.valueOf(val);
                try {
                    return URLEncoder.encode(valStr, GatewayConstants.UTF8);
                } catch (UnsupportedEncodingException e) {
                    return valStr;
                }
            }
        };
    }

    public static String wrapVal(Object val) {
        return wrapper.wrapVal(val);
    }

    interface Wrapper {
        default String wrapVal(Object val) {
            return String.valueOf(val);
        }
    }

}
