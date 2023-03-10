package com.cn.xiguaapp.datagrap.core.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xiguaapp
 * @date 2021-02-21
 * @desc app基本信息
 */
@Getter
@Setter
@ToString
public class AppBasicInfo {
    /**
     * 系统IP
     */
    private String host;
    /**
     * 系统启动路径
     */
    private String appPath;
    /**
     * 系统启动端口
     */
    private String port;
    /**
     * 系统状态（1:online   0:offline）
     */
    private Integer status;

    @Override
    public boolean equals(Object that) {
        if(that instanceof AppBasicInfo){
            AppBasicInfo t = (AppBasicInfo)that;
            return this.host.equals(t.getHost()) && this.port.equals(t.getPort());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.port.hashCode()*21+this.host.hashCode()*23;
    }
}
