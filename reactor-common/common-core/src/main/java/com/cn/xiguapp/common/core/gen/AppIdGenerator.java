/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/19 上午10:41 >
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

package com.cn.xiguapp.common.core.gen;

import com.cn.xiguapp.common.core.utils.SnowflakeIdGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/19
 * @desc 生成App_Id
 */
public class AppIdGenerator {
    public static synchronized String generatorVersion(int length){
        StringBuilder date = new StringBuilder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        String substring = SnowflakeIdGenerator.getInstance().toString().substring(10, 17);
        date.append(substring);
        return date.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
                String s = generatorVersion(3);
                System.out.println(s+":"+s.length());
        }
    }
}
