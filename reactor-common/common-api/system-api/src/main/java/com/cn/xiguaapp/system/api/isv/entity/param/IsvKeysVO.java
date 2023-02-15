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

package com.cn.xiguaapp.system.api.isv.entity.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiguaapp
 */
@Data
@Accessors(chain = true)
public class IsvKeysVO {

    /** appKey, 数据库字段：app_key */
    private String appKey;

    /** secret, 数据库字段：secret */
    private String secret;

    /** 秘钥格式，1：PKCS8(JAVA适用)，2：PKCS1(非JAVA适用), 数据库字段：key_format */
    private Byte keyFormat = 1;

    /** 开发者生成的公钥, 数据库字段：public_key_isv */
    private String publicKeyIsv;

    /** 开发者生成的私钥（交给开发者）, 数据库字段：private_key_isv */
    private String privateKeyIsv;

    /** 平台生成的公钥（交给开发者）, 数据库字段：public_key_platform */
    private String publicKeyPlatform;

    /** 平台生成的私钥, 数据库字段：private_key_platform */
    private String privateKeyPlatform;

//  "签名类型：1:RSA2,2:MD5")
    private Byte signType = 1;

}
