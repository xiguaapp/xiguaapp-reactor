package com.cn.xiguaapp.system.api.isv.entity.param;

import lombok.Data;
import org.checkerframework.checker.units.qual.Length;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/15
 * @desc
 */
@Data
public class IsvInfoForm {
    /*状态*/
    private Integer status = 0;
    /*内容、备注*/
    private String remark;
    /*roleCode数组*/
    private Flux<String> releCode = Flux.empty();
}
