/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/10 下午8:11 >
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

package com.cn.xiguaapp.system.api.isv.entity.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class IsvDefinition implements Isv {
    public static final int SIGN_TYPE_RSA2 = 1;

    public IsvDefinition() {
    }

    public IsvDefinition(String appKey, String secret) {
        this.appKey = appKey;
        this.secret = secret;
    }

    private String appKey;

    /** 秘钥,签名方式为MD5时有用 */
    private String secret;

    /** 开发者生成的公钥, 数据库字段：public_key_isv */
    private String publicKeyIsv;

    /** 平台生成的私钥, 数据库字段：private_key_platform */
    private String privateKeyPlatform;

    /** 0启用，1禁用 */
    private Integer status;

    /** 签名类型：1:RSA2,2:MD5 */
    private Integer signType = 1;

    @Override
    public String getSecretInfo() {
        return signType == SIGN_TYPE_RSA2 ? publicKeyIsv : secret;
    }

}
