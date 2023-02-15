package com.cn.xiguaapp.common.gateway.validate;

import com.cn.xiguapp.common.core.utils.sign.encrypt.AESUtil;
import com.cn.xiguapp.common.core.utils.sign.encrypt.RSANewUtil;
import com.cn.xiguapp.common.core.utils.sign.encrypt.RSAUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 负责各类加解密
 * @author xiguaapp
 *
 */
public class ApiEncrypter implements Encrypter {

    @Override
    public String aesEncryptToHex(String content, String password) throws Exception {
        return AESUtil.encryptToHex(content, password);
    }

    @Override
    public String aesDecryptFromHex(String hex, String password) throws Exception {
        return AESUtil.decryptFromHex(hex, password);
    }

    @Override
    public String aesEncryptToBase64String(String content, String password) throws Exception {
        return AESUtil.encryptToBase64String(content, password);
    }

    @Override
    public String aesDecryptFromBase64String(String base64String, String password) throws Exception {
        return AESUtil.decryptFromBase64String(base64String, password);
    }

    @Override
    public String rsaDecryptByPrivateKey(String data, String privateKey) throws Exception {
        return RSAUtil.decryptByPrivateKey(data, privateKey);
    }

    @Override
    public String rsaEncryptByPrivateKey(String data, String privateKey) throws Exception {
        return RSAUtil.encryptByPrivateKey(data, privateKey);
    }

    @Override
    public String rsaDecryptByPrivateKeyNew(String data, String privateKey){
        return RSANewUtil.decryptByPrivateKey(data, privateKey);
    }

    @Override
    public String rsaEncryptByPrivateKeyNew(String data, String privateKey){
        return RSANewUtil.encryptByPrivateKey(data, privateKey);
    }

    @Override
    public String md5(String value) {
        return DigestUtils.md5Hex(value);
    }

}
