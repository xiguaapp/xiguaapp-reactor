/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:04 >
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

package com.cn.ykyoung.server.bean;

import com.cn.ykyoung.server.common.RouteDefinition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author xiguaapp
 * @desc 服务请求接口api信息
 * @since 1.0 17:04
 */
@Data
public class ServiceApiInfo {
    //服务id
    private String serviceId;
    //详细信息结合
    private List<ApiMeta> apis;

    private List<RouteDefinition> routeDefinitionList;
    @Getter
    @Setter
    public static class ApiMeta {
        /** 接口名 */
        private String name;
        /** 请求path */
        private String path;
        /** 版本号 */
        private String version;
        /** 是否忽略验证 */
        private int ignoreValidate;
        /** 是否合并结果 */
        private int mergeResult;
        /** 是否需要授权才能访问 */
        private int permission;
        /** 是否需要token */
        private int needToken;

        public ApiMeta() {
        }

        public ApiMeta(String name, String path, String version) {
            this.name = name;
            this.path = path;
            this.version = version;
        }

        public String fetchNameVersion() {
            return this.name + this.version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ApiMeta apiMeta = (ApiMeta) o;
            return name.equals(apiMeta.name) &&
                    Objects.equals(version, apiMeta.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, version);
        }
    }
}
