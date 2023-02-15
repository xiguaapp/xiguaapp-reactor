package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser;

import com.cn.xiguaapp.r2dbc.orm.codec.ValueCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityPropertyDescriptor;

import java.util.Optional;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0
 */
public interface ValueCodecResolver {

    Optional<ValueCodec> resolve(EntityPropertyDescriptor descriptor);

}
