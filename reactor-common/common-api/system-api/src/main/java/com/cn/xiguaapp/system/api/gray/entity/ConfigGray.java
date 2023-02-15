/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:48 >
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

package com.cn.xiguaapp.system.api.gray.entity;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.GenericEntity;
import com.cn.xiguaapp.r2dbc.common.core.generator.Generators;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;


/**
 * 表名：config_gray
 * 备注：服务灰度配置
 *
 * @author xiguaapp
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "config_gray")
@Comment("服务灰度配置")
public class ConfigGray extends GenericEntity<Long> {
    @Override
    @GeneratedValue(generator = Generators.SNOW_FLAKE)
    public Long getId() {
        return super.getId();
    }
    @Column(length = 100)
    @Comment("服务id")
    @Schema(description = "服务id")
    private String serviceId;

    @Column(length = 255)
    @Comment("用户key，多个用引文逗号隔开")
    @Schema(description = "用户key，多个用引文逗号隔开")
    private String userKeyContent;

    @Column(length = 255)
    @Comment("需要灰度的接口，goods.get1.0=1.2，多个用英文逗号隔开")
    @Schema(description = "需要灰度的接口，goods.get1.0=1.2，多个用英文逗号隔开")
    private String nameVersionContent;
}
