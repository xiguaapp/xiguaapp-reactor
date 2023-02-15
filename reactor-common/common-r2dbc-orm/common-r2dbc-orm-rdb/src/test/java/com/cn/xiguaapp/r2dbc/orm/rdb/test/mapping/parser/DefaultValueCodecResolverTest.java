package com.cn.xiguaapp.r2dbc.orm.rdb.test.mapping.parser;

import com.cn.xiguaapp.r2dbc.orm.codec.ValueCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.codec.DateTimeCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.codec.EnumValueCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.codec.JsonValueCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.EnumCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.JsonCodec;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.jpa.SimpleEntityPropertyDescriptor;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser.DefaultValueCodecResolver;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultValueCodecResolverTest {


    @Test
    public void test(){
        DefaultValueCodecResolver resolver=DefaultValueCodecResolver.COMMONS;

        Map<String, PropertyDescriptor> descriptorMap = Stream.of(PropertyUtils.getPropertyDescriptors(Entity.class))
                .collect(Collectors.toMap(PropertyDescriptor::getName, Function.identity()));


        {
          ValueCodec codec= resolver.resolve(SimpleEntityPropertyDescriptor.of(descriptorMap.get("date"),null))
                    .orElseThrow(NullPointerException::new);

            Assert.assertTrue(codec instanceof DateTimeCodec);
        }

        {
            ValueCodec codec= resolver.resolve(SimpleEntityPropertyDescriptor.of(descriptorMap.get("arr"),null))
                    .orElseThrow(NullPointerException::new);

            Assert.assertTrue(codec instanceof JsonValueCodec);
        }

        {
            ValueCodec codec= resolver.resolve(SimpleEntityPropertyDescriptor.of(descriptorMap.get("state"),null))
                    .orElseThrow(NullPointerException::new);

            Assert.assertTrue(codec instanceof EnumValueCodec);
        }

        {
            ValueCodec codec= resolver.resolve(SimpleEntityPropertyDescriptor.of(descriptorMap.get("state2"),null))
                    .orElseThrow(NullPointerException::new);

            Assert.assertTrue(codec instanceof EnumValueCodec);
        }
    }

    @Getter
    @Setter
    public static class Entity {

        private String name;

        private Date date;

        @JsonCodec
        private List<String> arr;

        @EnumCodec(toMask = true)
        private State state;

        private State[] state2;

    }

    public enum State{
        A,B;
    }
}