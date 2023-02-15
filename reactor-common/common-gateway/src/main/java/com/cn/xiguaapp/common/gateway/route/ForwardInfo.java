/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午4:45 >
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

package com.cn.xiguaapp.common.gateway.route;

import com.cn.xiguaapp.common.gateway.bean.TargetRoute;
import lombok.Data;

/**
 * @author xiguaapp
 */
@Data
public class ForwardInfo {

    private TargetRoute targetRoute;

    public static ForwardInfo getErrorForwardInfo() {
        return ErrorForwardInfo.errorForwardInfo;
    }

    public ForwardInfo(TargetRoute targetRoute) {
        this.targetRoute = targetRoute;
    }

    public String getPath() {
        return targetRoute.getRouteDefinition().getPath();
    }

    public String getVersion() {
        return targetRoute.getRouteDefinition().getVersion();
    }

    static class ErrorForwardInfo extends ForwardInfo {

        private static final String VALIDATE_ERROR_PATH = "/sop/validateError";

        public static ErrorForwardInfo errorForwardInfo = new ErrorForwardInfo();

        public ErrorForwardInfo() {
            this(null);
        }

        public ErrorForwardInfo(TargetRoute targetRoute) {
            super(targetRoute);
        }

        @Override
        public String getPath() {
            return VALIDATE_ERROR_PATH;
        }

        @Override
        public String getVersion() {
            return "";
        }
    }

}
