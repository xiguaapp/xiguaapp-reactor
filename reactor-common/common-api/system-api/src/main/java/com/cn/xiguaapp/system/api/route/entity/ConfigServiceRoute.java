/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/10 上午10:50 >
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

package com.cn.xiguaapp.system.api.route.entity;

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.GenericEntity;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.Comment;
import com.cn.ykyoung.server.route.RouteDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;


/**
 * 表名：config_service_route
 * 备注：路由配置
 *
 * @author xiguaapp
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Table(name = "config_service_route")
@Comment("路由配置")
public class ConfigServiceRoute extends GenericEntity<Long> {

    @Comment("serviceId")
    @Schema(description = "serviceId")
    @Column(length = 200)
    private String serviceId;

    @Comment("接口名")
    @Schema(description = "接口名")
    @Column(length = 100)
    private String name;

    @Comment("版本号")
    @Schema(description = "版本号")
    @Column(length = 100)
    private String version;

    @Comment("路由断言（SpringCloudGateway专用）")
    @Schema(description = "路由断言（SpringCloudGateway专用）")
    @Column(length = 200)
    private String predicates;

    @Comment("路由过滤器（SpringCloudGateway专用）")
    @Schema(description = "路由过滤器（SpringCloudGateway专用）")
    @Column(length = 200)
    private String filters;

    @Comment("路由规则转发的目标uri")
    @Schema(description = "路由规则转发的目标uri")
    @Column(length = 255)
    private String uri;

    @Comment("uri后面跟的path")
    @Schema(description = "uri后面跟的path")
    @Column(length = 200)
    private String path;

    @Comment("路由执行的顺序")
    @Schema(description = "路由执行的顺序")
    @Column(length = 200)
    private Integer orderIndex;

    @Comment("是否忽略验证，业务参数验证除外,")
    @Schema(description = "是否忽略验证，业务参数验证除外,")
    @Column(length = 2)
    private Integer ignoreValidate;

    @Comment("状态，0：待审核，1：启用，2：禁用")
    @Schema(description = "状态，0：待审核，1：启用，2：禁用")
    @Column(length = 2)
    private Integer status;

    @Comment("是否合并结果,")
    @Schema(description = "是否合并结果,")
    @Column(length = 2)
    private Integer mergeResult;

    @Comment("是否需要授权才能访问")
    @Schema(description = "是否需要授权才能访问")
    @Column(length = 2)
    private Integer permission;

    @Comment("是否需要token,")
    @Schema(description = "是否需要token,")
    @Column(length = 2)
    private Integer needToken;

    public static ConfigServiceRoute of(String serviceId, RouteDefinition routeDefinition){
        ConfigServiceRoute configServiceRoute = new ConfigServiceRoute();
        configServiceRoute.setServiceId(routeDefinition.getId());
        configServiceRoute.setName(routeDefinition.getName());
        configServiceRoute.setVersion(routeDefinition.getVersion());
        configServiceRoute.setUri(routeDefinition.getUri());
        configServiceRoute.setPath(routeDefinition.getPath());
        configServiceRoute.setFilters(JSON.toJSONString(routeDefinition.getFilters()));
        configServiceRoute.setPredicates(JSON.toJSONString(routeDefinition.getPredicates()));
        configServiceRoute.setIgnoreValidate(routeDefinition.getIgnoreValidate());
        configServiceRoute.setMergeResult(routeDefinition.getMergeResult());
        configServiceRoute.setStatus(routeDefinition.getStatus());
        configServiceRoute.setPermission(routeDefinition.getPermission());
        configServiceRoute.setOrderIndex(routeDefinition.getOrder());
        configServiceRoute.setNeedToken(routeDefinition.getNeedToken());
        configServiceRoute.setServiceId(serviceId);
        return configServiceRoute;
    }
}
