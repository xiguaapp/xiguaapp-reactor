/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:15 >
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

package com.cn.xiguaapp.common.gateway.configuration;


import com.cn.xiguaapp.common.gateway.bean.ApiContext;
import com.cn.xiguaapp.common.gateway.bean.ParamNames;
import com.cn.xiguaapp.common.gateway.validate.taobao.TaobaoSigner;

/**
 * 具备淘宝开放平台能力配置
 * 淘宝开放平台：http://open.taobao.com/doc.htm
 * @author xiguaapp
 */
public class TaobaoGatewayConfiguration extends BaseGatewayConfiguration {

    static {
        ParamNames.APP_KEY_NAME = "app_key";
        ParamNames.SIGN_TYPE_NAME = "sign_method";
        ParamNames.VERSION_NAME = "v";
        ParamNames.APP_AUTH_TOKEN_NAME = "session";

        ApiContext.getApiConfig().setSigner(new TaobaoSigner());
    }

}
