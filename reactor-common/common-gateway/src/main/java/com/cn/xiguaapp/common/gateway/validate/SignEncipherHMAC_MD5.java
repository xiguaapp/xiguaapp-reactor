/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:07 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cn.xiguaapp.common.gateway.validate;

import com.cn.xiguaapp.common.gateway.bean.GatewayConstants;
import com.cn.xiguaapp.common.gateway.message.ErrorEnum;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * HMAC_MD5加密
 * @author xiguaapp
 */
@Slf4j
public class SignEncipherHMAC_MD5 implements SignEncipher {

    public static final String HMAC_MD5 = "HmacMD5";

    @Override
    public byte[] encrypt(String input, String secret) {
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(GatewayConstants.CHARSET_UTF8), HMAC_MD5);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return mac.doFinal(input.getBytes(GatewayConstants.CHARSET_UTF8));
        } catch (NoSuchAlgorithmException e) {
            log.error("HMAC_MD5加密加密失败NoSuchAlgorithmException", e);
            throw ErrorEnum.ISV_INVALID_SIGNATURE.getErrorMeta().getException();
        } catch (InvalidKeyException e) {
            log.error("HMAC_MD5加密加密失败InvalidKeyException", e);
            throw ErrorEnum.ISV_INVALID_SIGNATURE.getErrorMeta().getException();
        }
    }
}
