package com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.param;

import cn.hutool.core.bean.BeanUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 十一月
 * @desc
 */
@Getter
@Setter
@Deprecated
public class PageParams<T> extends PageRequest {
    private static int page = 0;
    private static int size = 10;
    private static Sort sort = Sort.by("asc");
    private Map<String,Object>requstMap = new HashMap<>();

    public PageParams(Map<String,Object> map){
        super(Integer.parseInt(Optional.ofNullable(map)
                        .orElseGet(HashMap::new)
                        .getOrDefault("page",page).toString()),
                Integer.parseInt(Optional.ofNullable(map)
                        .orElseGet(HashMap::new)
                        .getOrDefault("size",size).toString())
                ,sort);
        if (map == null){
            requstMap = new HashMap<>();
        }
        map.remove("page");
        map.remove("size");
        requstMap.putAll(map);
    }

    public <T> T mapToBean(Class<T> tClass){
        return BeanUtil.mapToBean(this.requstMap,tClass,true);
    }


}
