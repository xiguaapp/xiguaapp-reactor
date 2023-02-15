/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:54 >
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

package com.cn.ykyoung.server.common.param;

import com.cn.ykyoung.server.common.BaseValidationAnnotationBuilder;
import com.cn.ykyoung.server.common.ValidationAnnotationBuilder;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Currency;
import org.hibernate.validator.constraints.EAN;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.LuhnCheck;
import org.hibernate.validator.constraints.Mod10Check;
import org.hibernate.validator.constraints.Mod11Check;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.ScriptAssert;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiguaapp
 */
public class ValidationAnnotationDefinitionFactory {

    private static final Map<Class<?>, ValidationAnnotationBuilder<?>> store = new HashMap<>(64);

    static {
        new BaseValidationAnnotationBuilder<ApiModelProperty>(){};

        // validation-api-2.0.1.Final.jar下javax.validation.constraints
        new BaseValidationAnnotationBuilder<AssertFalse>(){};
        new BaseValidationAnnotationBuilder<AssertTrue>(){};
        new BaseValidationAnnotationBuilder<DecimalMax>(){};
        new BaseValidationAnnotationBuilder<DecimalMin>(){};
        new BaseValidationAnnotationBuilder<Digits>(){};
        new BaseValidationAnnotationBuilder<Email>(){};
        new BaseValidationAnnotationBuilder<Future>(){};
        new BaseValidationAnnotationBuilder<Max>(){};
        new BaseValidationAnnotationBuilder<Min>(){};
        new BaseValidationAnnotationBuilder<Negative>(){};
        new BaseValidationAnnotationBuilder<NegativeOrZero>(){};
        new BaseValidationAnnotationBuilder<NotBlank>(){};
        new BaseValidationAnnotationBuilder<NotEmpty>(){};
        new BaseValidationAnnotationBuilder<NotNull>(){};
        new BaseValidationAnnotationBuilder<Null>(){};
        new BaseValidationAnnotationBuilder<Past>(){};
        new BaseValidationAnnotationBuilder<PastOrPresent>(){};
        new BaseValidationAnnotationBuilder<Pattern>(){};
        new BaseValidationAnnotationBuilder<Positive>(){};
        new BaseValidationAnnotationBuilder<PositiveOrZero>(){};
        new BaseValidationAnnotationBuilder<Size>(){};

        // hibernate-validator-6.0.10.Final.jar下org.hibernate.validator.constraints
        new BaseValidationAnnotationBuilder<CodePointLength>(){};
        new BaseValidationAnnotationBuilder<ConstraintComposition>(){};
        new BaseValidationAnnotationBuilder<CreditCardNumber>(){};
        new BaseValidationAnnotationBuilder<Currency>(){};
        new BaseValidationAnnotationBuilder<EAN>(){};
        new BaseValidationAnnotationBuilder<ISBN>(){};
        new BaseValidationAnnotationBuilder<Length>(){};
        new BaseValidationAnnotationBuilder<LuhnCheck>(){};
        new BaseValidationAnnotationBuilder<Mod10Check>(){};
        new BaseValidationAnnotationBuilder<Mod11Check>(){};
        new BaseValidationAnnotationBuilder<ParameterScriptAssert>(){};
        new BaseValidationAnnotationBuilder<Range>(){};
        new BaseValidationAnnotationBuilder<SafeHtml>(){};
        new BaseValidationAnnotationBuilder<ScriptAssert>(){};
        new BaseValidationAnnotationBuilder<UniqueElements>(){};
        new BaseValidationAnnotationBuilder<URL>(){};
    }

    /**
     * 添加注解对应的构建器
     *
     * @param annoClass
     * @param builder
     */
    public static void addBuilder(Class<?> annoClass, ValidationAnnotationBuilder<?> builder) {
        store.put(annoClass, builder);
    }

    public static ValidationAnnotationDefinition build(Annotation annotation) {
        Class<?> jsr303Anno = annotation.annotationType();
        ValidationAnnotationBuilder validationAnnotationBuilder = store.get(jsr303Anno);
        if (validationAnnotationBuilder == null) {
            return null;
        }
        return validationAnnotationBuilder.build(annotation);
    }

}
