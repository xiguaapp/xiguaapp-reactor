package com.cn.xiguaapp.r2dbc.orm.rdb.codec;


import com.cn.xiguaapp.r2dbc.orm.codec.Decoder;
import com.cn.xiguaapp.r2dbc.orm.codec.Encoder;
import com.cn.xiguaapp.r2dbc.orm.codec.ValueCodec;

import java.util.LinkedList;

/**
 * @author xiguaapp
 */
public class CompositeValueCodec implements ValueCodec<Object, Object> {

    private LinkedList<Encoder> encoders = new LinkedList<>();
    private LinkedList<Decoder> decoders = new LinkedList<>();

    public CompositeValueCodec addEncoder(Encoder encoder){
        encoders.add(encoder);
        return this;
    }

    public CompositeValueCodec addDecoder(Decoder encoder){
        decoders.add(encoder);
        return this;
    }

    public CompositeValueCodec addEncoderFirst(Encoder encoder){
        encoders.addFirst(encoder);
        return this;
    }

    public CompositeValueCodec addDecoderFirst(Decoder encoder){
        decoders.addFirst(encoder);
        return this;
    }
    @Override
    public Object encode(Object value) {
        for (Encoder codec : encoders) {
            value = codec.encode(value);
        }
        return value;
    }

    @Override
    public Object decode(Object data) {

        for (Decoder codec : decoders) {
            data = codec.decode(data);
        }
        return data;
    }
}
