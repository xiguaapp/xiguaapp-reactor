package com.cn.xiguapp.common.core.utils;

import com.cn.xiguapp.common.core.exception.ValidationException;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/30
 * @desc 参数验证
 */
@NoArgsConstructor
public class ValidatorUtils {
    public static volatile Validator validator;
    public static Validator getValidator(){
        if (Objects.isNull(validator)){
            synchronized (ValidatorUtils.class){
                return Validation.buildDefaultValidatorFactory().getValidator();
            }
        }
        return validator;
    }

    public static <T> T tryValidate(T bean, Class... group) {
        Set<ConstraintViolation<T>> violations = getValidator().validate(bean, group);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.iterator().next().getMessage(), violations);
        }

        return bean;
    }
}
