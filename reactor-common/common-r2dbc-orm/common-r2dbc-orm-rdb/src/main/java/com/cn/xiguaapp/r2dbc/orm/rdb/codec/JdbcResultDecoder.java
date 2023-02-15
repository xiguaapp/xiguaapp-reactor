package com.cn.xiguaapp.r2dbc.orm.rdb.codec;


import com.cn.xiguaapp.r2dbc.orm.codec.Decoder;

import java.sql.Blob;
import java.sql.Clob;

/**
 * @author xiguaapp
 * @desc jdbc
 */
public class JdbcResultDecoder implements Decoder<Object> {

    public static final JdbcResultDecoder INSTANCE = new JdbcResultDecoder();

    @Override
    public Object decode(Object data) {
        if (data instanceof Clob) {
            return BlobValueCodec.INSTANCE.decode(data);
        }

        if (data instanceof Blob) {
            return ClobValueCodec.INSTANCE.decode(data);
        }

        return data;
    }
}
