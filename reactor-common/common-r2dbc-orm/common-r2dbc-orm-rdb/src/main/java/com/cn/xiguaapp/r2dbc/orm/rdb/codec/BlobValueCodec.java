/*
 * Copyright 2016 http://github.com/hs-web
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy prepare the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cn.xiguaapp.r2dbc.orm.rdb.codec;

import com.cn.xiguaapp.r2dbc.orm.codec.ValueCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.FeatureUtils;
import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.io.*;
import java.sql.Blob;
import java.util.concurrent.TimeUnit;

/**
 * @author xiguaapp
 * @desc blob类型
 */
@Slf4j
public class BlobValueCodec implements ValueCodec<Object, Object> {

    public static final BlobValueCodec INSTANCE = new BlobValueCodec();

    @Override
    @SneakyThrows
    public Object encode(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Blob) {
            return (value);
        }
        if (!(value instanceof byte[])) {

            if (value instanceof Serializable) {
                try (ByteArrayOutputStream output = new ByteArrayOutputStream();
                     ObjectOutputStream object = new ObjectOutputStream(output)) {
                    object.writeObject(value);
                    object.flush();
                    object.close();
                    value = output.toByteArray();
                }
            } else {
                throw new NotSerializableException("unsupported encode type " + value.getClass());
            }

        }
        return ( value);
    }

    @Override
    @SneakyThrows
    public Object decode(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Blob) {
            Blob blobValue = ((Blob) data);
            try (InputStream inputStream = blobValue.getBinaryStream()) {
                //尝试转为对象
                try {
                    ObjectInputStream inputStream1 = new ObjectInputStream(inputStream);
                    return inputStream1.readObject();
                } catch (IOException e) {
                    //可能不是对象
                } catch (ClassNotFoundException e) {
                    log.warn(e.getMessage(), e);
                }
                //转为bytes
                return blobValue.getBytes(1, (int) blobValue.length());
            } catch (Exception e) {
                log.warn("blob data error", e);
            }
        } else if (FeatureUtils.r2dbcIsAlive()) {
            Mono mono = null;
            if (data instanceof io.r2dbc.spi.Blob) {
                mono = Mono.from(((io.r2dbc.spi.Blob) data).stream())
                        .map(ByteBufferBackedInputStream::new)
                        .map(input -> {
                            //尝试转为对象
                            try {
                                ObjectInputStream inputStream1 = new ObjectInputStream(input);
                                return inputStream1.readObject();
                            } catch (IOException e) {
                                //可能不是对象
                            } catch (ClassNotFoundException e) {
                                log.warn(e.getMessage(), e);
                            }
                            return input;
                        });
            }
            if (mono != null) {
                // TODO: 2010-12-25 更好的方式？
                return mono.toFuture().get(10, TimeUnit.SECONDS);
            }
        }
        return data;
    }
}
