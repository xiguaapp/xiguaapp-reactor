/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/19 下午2:42 >
 *
 *     Send: 1125698980@qq.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguapp.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/19
 * @desc 叮叮机器人签名工具
 */
@Slf4j
public class DingtalkSign {
    /**
     * 默认钉钉签名
     * @see  {https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq}
     * @param timestamp {@link System#currentTimeMillis()}
     * @return
     */
    public static String sign(Long timestamp){
        return sign("SEC501e757cf46ee8c735793cb8af6161ab8f98a05ec1405a126f8d4d424e958685",timestamp);
    }

    /**
     * 钉钉签名
     * @param secret secret
     * @param timestamp 时间串
     * @return
     */
    public static String sign(String secret,Long timestamp){
        try {
            String stringToSign = timestamp+"\n"+ secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8);
            log.info("【钉钉机器人签名】{}",sign);
            return sign;
        }catch (Exception e){
            log.error("【钉钉机器人签名异常】{}",e.getMessage());
        }
        return null;
    }

}
