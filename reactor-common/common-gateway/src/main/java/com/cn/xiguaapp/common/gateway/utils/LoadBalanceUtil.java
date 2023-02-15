/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:26 >
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

package com.cn.xiguaapp.common.gateway.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author xiguaapp
 */
public class LoadBalanceUtil {

    /**
     * key：serviceId，value：指示变量i
     */
    private static Map<String, Integer> serviceIdRoundMap = new ConcurrentHashMap<>(8);

    /**
     * 轮询选择一台机器。<br>
     * <p>
     * 假设有N台服务器：S = {S1, S2, …, Sn}，一个指示变量i表示上一次选择的服务器ID。变量i被初始化为N-1。
     * </p>
     * 参考：https://blog.csdn.net/qq_37469055/article/details/87991327
     * @param serviceId serviceId，不同的serviceId对应的服务器数量不一样，需要区分开
     * @param servers   服务器列表
     * @return 返回一台服务器实例
     */
    public static <T> T chooseByRoundRobin(String serviceId, List<T> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        int n = servers.size();
        int i = serviceIdRoundMap.computeIfAbsent(serviceId, (k) -> n - 1);
        int j = i;
        do {
            j = (j + 1) % n;
            i = j;
            serviceIdRoundMap.put(serviceId, i);
            return servers.get(i);
        } while (j != i);
    }

    /**
     * 随机选取一台实例
     *
     * @param servers 服务列表
     * @return 返回实例，没有返回null
     */
    public static <T> T chooseByRandom(List<T> servers) {
        if (servers.isEmpty()) {
            return null;
        }
        int serverCount = servers.size();
        // 随机选取一台实例
        int index = chooseRandomInt(serverCount);
        return servers.get(index);
    }

    private static int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }

}
