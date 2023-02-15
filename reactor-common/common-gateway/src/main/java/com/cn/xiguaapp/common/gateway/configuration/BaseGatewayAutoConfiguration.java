/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:31 >
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

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.common.gateway.bean.ApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * @author xiguaapp
 */
@Slf4j
@EnableConfigurationProperties(ApiConfigProperties.class)
public class BaseGatewayAutoConfiguration {

    @Autowired
    private ApiConfigProperties apiConfigProperties;

    @PostConstruct
    public void after() {
        log.info("网关基本配置：{}", JSON.toJSONString(apiConfigProperties));
        ApiConfig apiConfig = ApiConfig.getInstance();
        BeanUtils.copyProperties(apiConfigProperties, apiConfig);
    }

}
