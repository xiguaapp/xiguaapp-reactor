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

package com.cn.ykyoung.server.bean;

/**
 * @author xiguaapp
 */
public class NacosConfigs {

    public static final String GROUP_CHANNEL = "sop:channel";

    public static final String GROUP_ROUTE = "sop:route";

    public static final String DATA_ID_GRAY = "com.gitee.sop.channel.gray";

    public static final String DATA_ID_IP_BLACKLIST = "com.gitee.sop.channel.ipblacklist";

    public static final String DATA_ID_ISV = "com.gitee.sop.channel.isv";

    public static final String DATA_ID_ROUTE_PERMISSION = "com.gitee.sop.channel.routepermission";

    public static final String DATA_ID_LIMIT_CONFIG = "com.gitee.sop.channel.limitconfig";

    public static final String DATA_ID_ROUTE_CONFIG = "com.gitee.sop.channel.routeconfig";

    private static final String DATA_ID_TPL = "com.gitee.sop.route.%s";

    public static String getRouteDataId(String serviceId) {
        return String.format(DATA_ID_TPL, serviceId);
    }
}
