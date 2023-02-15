/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:17 >
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

package com.cn.ykyoung.server.common.utils;

import com.cn.ykyoung.server.bean.ServiceContext;
import com.cn.ykyoung.server.exception.ServiceException;
import com.cn.ykyoung.server.message.ServiceErrorEnum;
import com.cn.ykyoung.server.message.ServiceErrorFactory;
import com.cn.ykyoung.server.param.validation.ValidationGroupSequence;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author xiguaapp
 * @desc 业务参数验证接口实现 JSR-303
 * @since 1.0 17:17
 */
public class ServiceParamValidator implements ParamValidator {
    private static final String LEFT_TOKEN = "{";
    private static final String RIGHT_TOKEN = "}";
    private static final String EQ = "=";
    private static final String COMMA = ",";
    private static Object[] EMPTY_OBJ_ARRAY = {};

    private static final List<String> SYSTEM_PACKAGE_LIST = Arrays.asList("java.lang", "java.math", "java.util", "sun.util");

    private static javax.validation.Validator validator;
    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private List<Object> listObjectField(Object object) {
        List<Object> ret = new ArrayList<>();
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            ret.add(field.get(object));
        }, this::isMatchField);
        return ret;
    }

    /**
     * 匹配校验字段。
     *
     * 1. 不为基本类型；
     * 2. 不为java自带的类型；
     * 3. 不为枚举
     * 4. 不为Map
     * @param field field
     * @return true，是自定义的
     */
    private boolean isMatchField(Field field) {
        Class<?> fieldType = field.getType();
        if (fieldType.isPrimitive()) {
            return false;
        }
        if (Map.class.isAssignableFrom(fieldType)) {
            return false;
        }
        Class<?> declaringClass = field.getDeclaringClass();
        boolean isSame = declaringClass == fieldType;
        boolean isAssignableFrom = declaringClass.isAssignableFrom(fieldType)
                || fieldType.isAssignableFrom(declaringClass);
        // 如果是相同类，或者有继承关系不校验。
        if (isSame || isAssignableFrom) {
            return false;
        }
        Package aPackage = fieldType.getPackage();
        if (aPackage == null) {
            return false;
        }
        String packageName = aPackage.getName();
        for (String prefix : SYSTEM_PACKAGE_LIST) {
            if (packageName.startsWith(prefix)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void validateBizParam(Object obj) {
        if (Objects.isNull(obj)){
            return;
        }
        //校验参数对象
        List<Object> fields = listObjectField(obj);
        if (fields.size()>0){
            fields.forEach(this::validateBizParam);
        }
        Set<ConstraintViolation<Object>> set = validator.validate(obj, ValidationGroupSequence.class);
        if (!CollectionUtils.isEmpty(set)) {
            ConstraintViolation<Object> oneError = set.iterator().next();
            String errorMsg = oneError.getMessage();
            throw this.getValidateBizParamException(errorMsg);
        }
    }

    private RuntimeException getValidateBizParamException(String errorMsg) {
        String subCode = ServiceErrorEnum.ISV_PARAM_ERROR.getErrorMeta().getSubCode();
        String[] msgToken = errorMsg.split(EQ);
        String msg = msgToken[0];
        if (msg.startsWith(LEFT_TOKEN) && msg.endsWith(RIGHT_TOKEN)) {
            String module = msg.substring(1, msg.length() - 1);
            Object[] params = this.buildParams(msgToken);
            String error = ServiceErrorFactory.getErrorMessage(module, ServiceContext.getCurrentContext().getLocale(), params);
            return new ServiceException(subCode, error);
        } else {
            return new ServiceException(subCode, errorMsg);
        }
    }

    private Object[] buildParams(String[] msgToken) {
        if (msgToken.length == 2) {
            return msgToken[1].split(COMMA);
        } else {
            return EMPTY_OBJ_ARRAY;
        }
    }
}
