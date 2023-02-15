/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午10:28 >
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

package com.cn.xiguaapp.common.gateway.manage;

import com.cn.xiguaapp.common.gateway.bean.ApiConfig;
import com.cn.xiguaapp.common.gateway.bean.ErrorDefinition;
import com.cn.xiguaapp.common.gateway.bean.ErrorEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 */
public class DefaultServiceErrorManager implements ServiceErrorManager {

    private static Map<String, ErrorEntity> store = new ConcurrentHashMap<>(128);

    @Override
    public Collection<ErrorEntity> listAllErrors() {
        return store.values();
    }

    @Override
    public void saveBizError(ErrorDefinition errorDefinition) {

    }

    @Override
    public void saveUnknownError(ErrorDefinition errorDefinition) {
        boolean hasCapacity = store.size() < ApiConfig.getInstance().getStoreErrorCapacity();
        // 这里还可以做其它事情，比如错误量到达一定数目后，自动发送邮件/微信给开发人员，方便及时获取异常情况
        String id = this.buildId(errorDefinition);
        ErrorEntity errorEntity = store.get(id);
        if (errorEntity == null && hasCapacity) {
            errorEntity = new ErrorEntity();
            BeanUtils.copyProperties(errorDefinition, errorEntity);
            errorEntity.setId(id);
            store.put(id, errorEntity);
        }
        if (errorEntity != null) {
            errorEntity.setCount(errorEntity.getCount() + 1);
        }
    }

    @Override
    public void clear() {
        store.clear();
    }

    protected String buildId(ErrorDefinition errorDefinition) {
        return DigestUtils.md5Hex(errorDefinition.getServiceId() + errorDefinition.getErrorMsg());
    }

}
