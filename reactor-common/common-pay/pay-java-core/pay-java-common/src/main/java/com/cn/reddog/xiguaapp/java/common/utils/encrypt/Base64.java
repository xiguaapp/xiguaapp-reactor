package com.cn.xiguaapp.xiguaapp.java.common.utils.encrypt;

/**
 * @author xiguaapp
 * @desc base64加密
 * @since 1.0 17:35
 */
public class Base64 {
    private Base64() {}

    public static byte[] decode(String str) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(str);
    }

    public static String encode(byte[] bytes) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }
}
