package com.cn.xiguapp.common.core.utils.sign.encrypt;

import com.cn.xiguapp.common.core.constant.CommonConstants;
import com.cn.xiguapp.common.core.utils.KeyStore;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author xiguaapp
 * @Date 2020/9/24
 * @desc RSA加解密工具
 */
public class RSANewUtil {
    public static final String RSA_ALGORITHM = "RSA";
    public static final String UTF8 = "UTF-8";

    /**
     * 创建公钥私钥
     *
     * @return 返回公私钥对
     * @throws Exception
     */
    public static KeyStore createKeys() {
        KeyPairGenerator keyPairGeno = null;
        try {
            keyPairGeno = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGeno.initialize(1024);
        KeyPair keyPair = keyPairGeno.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        KeyStore keyStore = new KeyStore();
        keyStore.setPublicKey(Base64.encodeBase64String(publicKey.getEncoded()));
        keyStore.setPrivateKey(Base64.encodeBase64String(privateKey.getEncoded()));
        return keyStore;
    }

    /**
     * 获取公钥对象
     *
     * @param pubKeyData 公钥数据
     * @return 公钥对象
     */
    public static RSAPublicKey getPublicKey(byte[] pubKeyData) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyData);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取公钥对象
     *
     * @param pubKey
     *            公钥
     * @return 返回公钥对象
     */
    public static RSAPublicKey getPublicKey(String pubKey) {
        return getPublicKey(Base64.decodeBase64(pubKey));

    }

    /**
     * 获取私钥对象
     *
     * @param priKey
     *            私钥
     * @return 返回私钥对象
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String priKey) {
        try {
            return getPrivateKey(Base64.decodeBase64(priKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes 私钥数据
     * @return 返回公钥对象
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String encryptByPublicKey(String data, String publicKey) {
        try {
            return encryptByPublicKey(data, getPublicKey(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 公钥加密
     *
     * @param data 内容
     * @param publicKey 公钥
     * @return 返回密文
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] bytes = new byte[0];
        try {
            bytes = cipher.doFinal(data.getBytes(UTF8));
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(bytes);
    }

    public static String decryptByPublicKey(String data, String rsaPublicKey) {
        try {
            return decryptByPublicKey(data, getPublicKey(rsaPublicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 公钥解密
     *
     * @param data 待解密内容
     * @param rsaPublicKey 公钥
     * @return 返回明文
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, RSAPublicKey rsaPublicKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] inputData = Base64.decodeBase64(data);
        byte[] bytes = new byte[0];
        try {
            bytes = cipher.doFinal(inputData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        try {
            return new String(bytes, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String encryptByPrivateKey(String data, String privateKey) {
        return encryptByPrivateKey(data, getPrivateKey(privateKey));
    }

    /**
     * 私钥加密
     *
     * @param data 内容
     * @param privateKey 私钥
     * @return 返回密文
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, RSAPrivateKey privateKey){
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] bytes = new byte[0];
        try {
            bytes = cipher.doFinal(data.getBytes(UTF8));
        } catch (IllegalBlockSizeException | UnsupportedEncodingException | BadPaddingException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(bytes);
    }

    public static String decryptByPrivateKey(String data, String privateKey) {
        try {
            return decryptByPrivateKey(data, getPrivateKey(privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 私钥解密
     *
     * @param data 待解密内容
     * @param privateKey 私钥
     * @return 返回明文
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] inputData = Base64.decodeBase64(data);
        byte[] bytes = new byte[0];
        try {
            bytes = cipher.doFinal(inputData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        try {
            return new String(bytes, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }


    /*
    pubKey:
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCG/iIZZzb16PxKqslkDMYa4tVFb3IVPBpLj4BgHQmDfe843sG4gkJIPXCm7+t6QxIbfDfynBpqZJLvu0c6E7TqlCtynBIlRFOBZrQVNEFkaanR2Kln3vd3CIidR571UstOC32XDyqAQNlvjD19zeIDVfmLa0Q+Or0zaxY99QwBHwIDAQAB
priKey:
MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIb+IhlnNvXo/EqqyWQMxhri1UVvchU8GkuPgGAdCYN97zjewbiCQkg9cKbv63pDEht8N/KcGmpkku+7RzoTtOqUK3KcEiVEU4FmtBU0QWRpqdHYqWfe93cIiJ1HnvVSy04LfZcPKoBA2W+MPX3N4gNV+YtrRD46vTNrFj31DAEfAgMBAAECgYBiNPQdwwcq86rHr2QAE4L0AF3ju+YlKKqAmg9s3PMU5ENq/jO0xZ7u6zPPXu/S7IR51m7lY0ecazqyiW6SA9AzYH7ImWWkZ4stZ03beTB2US3cSeJIkugoexoN5fQRAGZiZezTLs91CeJivESOZyDKnnQdgJ49mveBV5OvievD8QJBAMztpqiWWavdR4tqQ+plat+rwYoXqejsK3Hyfg0pVJqEdazve2sr74rla7yI9P47ZAh1sklCv0CO//ctICv366UCQQCoop3T0FeZtbKJG+fHzZvpAe63tXpdhLMaQvTBuXLG8vi78Wyfhg5r7HOWR0Z1V7nzF1gzMywL53Pmkq9tB65zAkAiHu/A4kfL9ewTqn3kaT6CP3baJ1aDEc+qCVYzms4bbDKruLQ0A/y+g7SMj8E7E2h0gCRPTm3JsgWsgjb5Gy6BAkAA8mjQd6sGQe7utilnBdCKTmh4v5wgSk53J0kYjWIHm/WpmIFzo90Q3hMIFP5gSk3Q/6CPKQpmRrZv5QL3KcPhAkEAuMoQbij/7hyLlIxRHZs2SMXxfHPiZgDc6rVi1KNxeq8HXTlERi7Npc2Uz5TeWN4JwBBx9uA50zowk9iS05nclQ==
用公钥加密mi : c3B0jtMdvkqrgaPxHZCK2cXMUQC2QzLud2ouLMNx0nBAj9k2/ytOuVJViTGe/DozB/ky5jvl4spD9Ey6aTMrwLHfQVhn0gRJ+wHcmx/51dXQDIgsldt6bf7YpdPdnghBjQz2+P5RhqSkeFDbTZKkl2BNaLE78a/OyWWeCGwN+4s=
true
用私钥加密mi2 : QU5vDnQ1ukj8GsauokFlgcB/g61U882tj82wHGrrqHEnvaga+4cXjML9RhjpZtKqwDGZTCujsmpynDk4qek6IGOQ/oxdWLwV4ZNjfa/oqA8OFDothVUT8wpqCu9kOYHrTdGybmXD0dB2Iy1/AMQTAgPNNXXiRXdvsz9xWYTV6z8=
true
     */
    public static void main(String[] args) {
        KeyStore keys = createKeys();
        String pubKey = keys.getPublicKey();
        System.out.println("pubKey:");
        System.out.println(pubKey);
        String priKey = keys.getPrivateKey();
        System.out.println("priKey:");
        System.out.println(priKey);

        String ming = "1234567890123456";
        // 用公钥加密
        String mi = encryptByPublicKey(ming, pubKey);
        System.out.println("用公钥加密mi : " + mi);
        // 用私钥解密
        System.out.println(ming.equals(decryptByPrivateKey(mi, priKey)));

        // 用私钥加密
        String mi2 = encryptByPrivateKey(ming, priKey);

        System.out.println("用私钥加密mi2 : " + mi2);
        // 用公钥解密
        String ming2 = decryptByPublicKey(mi2, pubKey);
        System.out.println(ming.equals(ming2));


        String s = encryptByPrivateKey(ming, CommonConstants.PRI_KEY);
        System.out.println("私钥加密xxxxx:"+s);
        String s1 = decryptByPublicKey(s, CommonConstants.PUB_KEY);
        System.out.println("私钥解密xxxxx:"+s1);

    }
}
