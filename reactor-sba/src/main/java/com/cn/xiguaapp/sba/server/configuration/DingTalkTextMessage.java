/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/19 下午4:27 >
 *
 *     Send: 1125698980@qq.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.sba.server.configuration;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/19
 * @desc 钉钉文本消息提示
 */
@Builder
@Data
public class DingTalkTextMessage implements DefaultDingTalkMessage {
    /**
     * 内容
     */
    private String content;
    /**
     * 被@人的手机号（在content里添加@人的手机号）可为空
     */
    private List<String>atMobiles;
    /**
     * 是否@所有人  可为空
     */
    private boolean isAtAll;

}
