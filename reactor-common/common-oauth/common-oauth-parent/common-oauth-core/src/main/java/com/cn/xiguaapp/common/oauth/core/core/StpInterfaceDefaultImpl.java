package com.cn.xiguaapp.common.oauth.core.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiguaapp
 * @desc 对StpInterface接口默认的实现类
 *   如果开发者没有实现StpInterface接口，则使用此默认实现
 * @since
 */
public class StpInterfaceDefaultImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        return new ArrayList<String>();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        return new ArrayList<String>();
    }

}
