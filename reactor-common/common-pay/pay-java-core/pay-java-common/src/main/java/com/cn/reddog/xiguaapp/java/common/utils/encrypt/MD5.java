package com.cn.xiguaapp.xiguaapp.java.common.utils.encrypt;

import com.cn.xiguapp.common.core.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import static com.cn.xiguapp.common.core.utils.StringUtils.getContentBytes;

/**
 * @author xiguaapp
 * @desc md5加密
 * @since 1.0 17:36
 */
public class MD5 {
    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param inputCharset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String inputCharset) {
        //拼接key
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, inputCharset));
    }

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param inputCharset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String inputCharset) {
        //判断是否一样
        return StringUtils.equals(sign(text, key, inputCharset).toUpperCase(), sign.toUpperCase());
    }
}
