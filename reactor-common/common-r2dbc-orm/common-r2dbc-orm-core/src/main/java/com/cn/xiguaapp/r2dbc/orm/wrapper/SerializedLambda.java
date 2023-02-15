/*
 *        Copyright (C) <2018-2028>  <@author: xiguaapp @date: @today>
 *        Send: 1125698980@qq.com
 *        This program is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *        This program is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *        You should have received a copy of the GNU General Public License
 *        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.r2dbc.orm.wrapper;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 20:06
 */
@Getter
@Slf4j
public class SerializedLambda implements Serializable {
    private static final long serialVersionUID = 8025925345765570181L;
    private Class<?> capturingClass;
    private String functionalInterfaceClass;
    private String functionalInterfaceMethodName;
    private String functionalInterfaceMethodSignature;
    private String implClass;
    private String implMethodName;
    private String implMethodSignature;
    private int implMethodKind;
    private String instantiatedMethodType;
    private Object[] capturedArgs;

    public String getMethodName() {
        return implMethodName;
    }

    @SneakyThrows
    public static SerializedLambda of(Object lambdaColumn) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(lambdaColumn);
                oos.flush();
                byte[] data = baos.toByteArray();
                try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(data)) {
                    @Override
                    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                        Class<?> clazz = super.resolveClass(objectStreamClass);
                        return clazz == java.lang.invoke.SerializedLambda.class ? SerializedLambda.class : clazz;
                    }
                }) {
                    SerializedLambda lambda = (SerializedLambda) objIn.readObject();
                    if (lambda.getMethodName().startsWith("lambda$")) {
                        throw new UnsupportedOperationException("请使用方法引用,例如: UserEntity::getName");
                    }
                    return lambda;
                }
            }
        } catch (NotSerializableException e) {

            throw new UnsupportedOperationException("请将类[" + e.getMessage() + "]实现[Serializable]接口");
        }
    }
}
