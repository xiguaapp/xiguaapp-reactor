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

/**
 * @author xiguaapp
 */
@Data
public class RoleVO {
//	@ApiDocField(description = "id")
	private Long id;

//	@ApiDocField(description = "角色码")
	private String roleCode;

//	@ApiDocField(description = "描述")
	private String description;

//	@ApiDocField(description = "创建时间")
	private LocalDateTime createTime;

//	@ApiDocField(description = "修改时间")
	private LocalDateTime updateTime;
}
