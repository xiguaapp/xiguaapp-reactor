/*
 *        Copyright (C) <2018-2028>  <@author: xiguaapp @date: @today>
 *        Send: 1125698980@qq.com
 *        This program is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *        This program is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *        You should have received a copy of the GNU General Public License
 *        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguapp.common.core.utils.sign.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加解密工具<br>
 *
 * @author xiguaapp
 */
public class RSATool {
    public static String RSA_ALGORITHM = "RSA";

    private KeyFormat keyFormat;
    private KeyLength keyLength;

    public RSATool(KeyFormat keyFormat, KeyLength keyLength) {
        this.keyFormat = keyFormat;
        this.keyLength = keyLength;
    }

    /**
     * 创建公钥私钥
     *
     * @return 返回公私钥对
     * @throws Exception
     */
    public KeyStore createKeys() throws Exception {
        KeyPairGenerator keyPairGeno = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGeno.initialize(keyLength.getLength());
        KeyPair keyPair = keyPairGeno.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        KeyStore keyStore = new KeyStore();
        if (this.keyFormat == KeyFormat.PKCS1) {
            keyStore.setPublicKey(Base64.encodeBase64String(publicKey.getEncoded()));
            keyStore.setPrivateKey(convertPkcs8ToPkcs1(privateKey.getEncoded()));
        } else {
            keyStore.setPublicKey(Base64.encodeBase64String(publicKey.getEncoded()));
            keyStore.setPrivateKey(Base64.encodeBase64String(privateKey.getEncoded()));
        }
        return keyStore;
    }

    /**
     * 获取公钥对象
     *
     * @param pubKeyData 公钥
     * @return 返回公钥对象
     * @throws Exception
     */
    public RSAPublicKey getPublicKey(byte[] pubKeyData) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取公钥对象
     *
     * @param pubKey 公钥
     * @return 返回私钥对象
     * @throws Exception
     */
    public RSAPublicKey getPublicKey(String pubKey) throws Exception {
        return getPublicKey(Base64.decodeBase64(pubKey));

    }

    /**
     * 获取私钥对象
     *
     * @param priKey 私钥
     * @return 私钥对象
     * @throws Exception
     */
    public RSAPrivateKey getPrivateKey(String priKey) throws Exception {
        return getPrivateKey(Base64.decodeBase64(priKey));
    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return 返回私钥
     * @throws Exception
     */
    public RSAPrivateKey getPrivateKey(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

    }

    /**
     * 公钥加密
     *
     * @param data      待加密内容
     * @param publicKey 公钥
     * @return 返回密文
     * @throws Exception
     */
    public String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(keyFormat.getCipherAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        // 如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    public String encryptByPrivateKey(String data, String privateKey) throws Exception {
        return encryptByPrivateKey(data, getPrivateKey(privateKey));
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return 返回密文
     * @throws Exception
     */
    public String encryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(keyFormat.getCipherAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        // 模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        // 如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    public String decryptByPrivateKey(String data, String privateKey) throws Exception {
        return decryptByPrivateKey(data, getPrivateKey(privateKey));
    }

    /**
     * 私钥解密
     *
     * @param data       待解密内容
     * @param privateKey 私钥
     * @return 返回明文
     * @throws Exception
     */
    public String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(keyFormat.getCipherAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        // 如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    /**
     * 公钥解密
     *
     * @param data         待解密内容
     * @param rsaPublicKey 公钥
     * @return 返回明文
     * @throws Exception
     */
    public String decryptByPublicKey(String data, RSAPublicKey rsaPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(keyFormat.getCipherAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
        // 模长
        int key_len = rsaPublicKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        // 如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    public static String convertPkcs8ToPkcs1(byte[] privateKeyData) throws Exception{
        PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privateKeyData);
        ASN1Encodable encodable = pkInfo.parsePrivateKey();
        ASN1Primitive primitive = encodable.toASN1Primitive();
        byte[] privateKeyPKCS1 = primitive.getEncoded();
        return Base64.encodeBase64String(privateKeyPKCS1);
    }


    /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++]) & 0xff) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9')) {
            bcd = (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            bcd = (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            bcd = (byte) (asc - 'a' + 10);
        } else {
            bcd = (byte) (asc - 48);
        }
        return bcd;
    }

    /**
     * BCD转字符串
     */
    public String bcd2Str(byte[] bytes) {
        char[] temp = new char[bytes.length * 2];
        char val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    public String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    public byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public enum KeyLength {
        /**
         * 秘钥长度：1024
         */
        LENGTH_1024(1024),
        /**
         * 秘钥长度：2048
         */
        LENGTH_2048(2048);
        private int length;

        KeyLength(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }
    }

    public static class KeyStore {
        private String publicKey;
        private String privateKey;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }

    public enum KeyFormat {
        PKCS1("RSA/ECB/PKCS1Padding"), PKCS8("RSA");
        private String cipherAlgorithm;

        KeyFormat(String cipherAlgorithm) {
            this.cipherAlgorithm = cipherAlgorithm;
        }

        public String getCipherAlgorithm() {
            return cipherAlgorithm;
        }
    }

    public KeyFormat getKeyFormat() {
        return keyFormat;
    }

    public KeyLength getKeyLength() {
        return keyLength;
    }



    /* ------------ Test ------------
    public static void main(String[] args) throws Exception {

        RSATool rsa_pkcs8_1024 = new RSATool(KeyFormat.PKCS8, KeyLength.LENGTH_1024);
        RSATool rsa_pkcs8_2048 = new RSATool(KeyFormat.PKCS8, KeyLength.LENGTH_2048);

        doTest(rsa_pkcs8_1024);
        doTest(rsa_pkcs8_2048);

        // PKCS在Java环境无法测试，可以生成一对，到非java平台测试，如C#
    }

    private static void doTest(RSATool rsaTool) throws Exception {
        System.out.println("秘钥格式：" + rsaTool.keyFormat.name() + ", 秘钥长度：" + rsaTool.keyLength.getLength());
        KeyStore keys = rsaTool.createKeys();
        String pubKey = keys.getPublicKey();
        System.out.println("pubKey:");
        System.out.println(pubKey);
        String priKey = keys.getPrivateKey();
        System.out.println("priKey:");
        System.out.println(priKey);
        System.out.println("--------");

        String ming = "你好，abc123~!@=";
        // 用公钥加密
        String mi = rsaTool.encryptByPublicKey(ming, rsaTool.getPublicKey(pubKey));
        System.out.println("mi : " + mi);
        // 用私钥解密
        String ming2 = rsaTool.decryptByPrivateKey(mi, rsaTool.getPrivateKey(priKey));
        System.out.println("ming : " + ming2 + ", 结果：" + ming2.equals(ming));

        // 用私钥加密
        String mi2 = rsaTool.encryptByPrivateKey(ming, rsaTool.getPrivateKey(priKey));

        System.out.println("mi2 : " + mi2);
        // 用公钥解密
        String ming3 = rsaTool.decryptByPublicKey(mi2, rsaTool.getPublicKey(pubKey));
        System.out.println("ming3 : " + ming3 + ", 结果：" + ming3.equals(ming));
        System.out.println("---------------------");
    }
    */

}