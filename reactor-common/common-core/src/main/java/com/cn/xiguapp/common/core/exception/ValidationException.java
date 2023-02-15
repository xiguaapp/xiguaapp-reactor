package com.cn.xiguapp.common.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc
 */
@Getter
@Setter
public class ValidationException extends RuntimeException{
    private List<Detail> details;

    public ValidationException(String message){
        super(message);
    }
    public ValidationException(String property, String message) {
        this(message, Collections.singletonList(new Detail(property, message, null)));
    }

    public ValidationException(String message,List<Detail>details){
        super(message);
        this.details = details;
    }
    public ValidationException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        if (null != violations && !violations.isEmpty()) {
            details = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                details.add(new Detail(violation.getPropertyPath().toString(), violation.getMessage(), null));
            }
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Detail {
//        @Schema(description = "字段")
        String property;

//        @Schema(description = "说明")
        String message;

//        @Schema(description = "详情")
        Object detail;
    }
}
