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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author xiguaapp
 */
@Data
public class IsvInfoVO {
    /**  数据库字段：id */
//    @ApiDocField(description = "id", example = "1")
    private Long id;

    /** appKey, 数据库字段：app_key */
//    @ApiDocField(description = "appKey", example = "aaaa")
    private String appKey;

    /** 0启用，1禁用, 数据库字段：status */
//    @ApiDocField(description = "状态：0启用，1禁用")
    private Byte status;

//    @ApiDocField(description = "签名类型：1:RSA2,2:MD5")
    private Byte signType;

//    @ApiDocField(description = "备注")
    private String remark;

    private Long userId;

    /**  数据库字段：gmt_create */
//    @ApiDocField(description = "添加时间")
    private LocalDateTime createTime;

    /**  数据库字段：gmt_modified */
//    @ApiDocField(description = "修改时间")
    private LocalDateTime updateTime;
//    @ApiDocField(description = "角色列表")
    private List<RoleVO> roleList;
}
