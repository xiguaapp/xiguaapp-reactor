package com.cn.xiguapp.common.core.utils;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;
import java.util.Properties;

/**
 * @author xiguaapp
 * @date 2021-02-21
 * @desc 目前只支持读取classpath下的yml文件
 */
public class YamlUtils {
    /**
     * 读取yml为map
     * @param path 路径
     * @return map
     */
    public static Map<String,Object> getMapFromYmlFile(String path){
        ClassPathResource resource = new ClassPathResource(path);
        YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
        yamlMapFactoryBean.setResources(resource);
        return yamlMapFactoryBean.getObject();
    }

    /**
     * 读取yml为prop
     * @param path 路径
     * @return map
     */
    public static Properties getPropFromYmlFile(String path){
        ClassPathResource resource = new ClassPathResource(path);
        YamlPropertiesFactoryBean yamlMapFactoryBean = new YamlPropertiesFactoryBean();
        yamlMapFactoryBean.setResources(resource);
        return yamlMapFactoryBean.getObject();
    }
}
