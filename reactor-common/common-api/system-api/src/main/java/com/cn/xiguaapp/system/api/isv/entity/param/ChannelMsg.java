/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/10 下午8:10 >
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

package com.cn.xiguaapp.system.api.isv.entity.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc 渠道实体
 */
@Data
public class ChannelMsg {

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss:SSS";

    public ChannelMsg(ChannelOperation channelOperation, JSONObject data) {
        this.operation = channelOperation.getOperation();
        this.data = data;
        this.timestamp = DateFormatUtils.format(new Date(), TIME_PATTERN);
    }
    /**
     * 渠道名称
     */
    private String operation;
    /**
     * 实体(数据)
     */
    private JSONObject data;
    /**
     * 加个时间戳，格式yyyy-MM-dd HH:mm:ss:SSS，确保每次推送内容都不一样
     * nacos监听基于MD5值，如果每次推送的内容一样，则监听不会触发，因此必须确保每次推送的MD5不一样
     */
    private String timestamp;

    /**
     * json转实体
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toObject(Class<T> clazz) {
        return data.toJavaObject(clazz);
    }
}
