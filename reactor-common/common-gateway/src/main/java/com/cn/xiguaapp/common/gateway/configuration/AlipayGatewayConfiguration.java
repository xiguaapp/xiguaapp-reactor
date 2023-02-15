/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:32 >
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
import com.cn.xiguaapp.common.gateway.validate.alipay.AlipaySigner;
import org.springframework.context.annotation.Configuration;

/**
 * 具备支付宝开放平台能力配置 https://docs.open.alipay.com/api
 *
 * @author xiguaapp
 */
@Configuration
public class AlipayGatewayConfiguration extends BaseGatewayConfiguration {

    static {
        ApiContext.getApiConfig().setSigner(new AlipaySigner());
    }
}
