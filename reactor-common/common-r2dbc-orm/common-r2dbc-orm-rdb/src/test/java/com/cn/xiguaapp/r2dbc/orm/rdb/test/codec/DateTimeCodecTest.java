package com.cn.xiguaapp.r2dbc.orm.rdb.test.codec;

import com.cn.xiguaapp.r2dbc.orm.rdb.codec.DateTimeCodec;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DateTimeCodecTest {

    @Test
    public void testDecodeString() {
        DateTimeCodec codec = new DateTimeCodec("yyyy:MM:dd", String.class);

        Date data = new Date();

        Object val = codec.encode(data);
        assertEquals(data, val);

        assertEquals(codec.decode(codec.encode("2019:01:01")), "2019:01:01");

    }


    @Test
    public void testCodeList() {
        DateTimeCodec codec = new DateTimeCodec("yyyy-MM-dd", String.class);

        String str = "2019-01-01,2019-01-31";

        Object encode = codec.encode(str);
        Assert.assertTrue(encode instanceof List);

        Assert.assertEquals(str,codec.decode(encode));
    }

    @Test
    public void testDeCodeList() {
        DateTimeCodec codec = new DateTimeCodec("yyyy-MM-dd", Date.class);

        String str = "2019-01-01,2019-01-31";

        Object decode = codec.decode(str);
        System.out.println((List)decode);
        Assert.assertTrue(decode instanceof List);

        Assert.assertArrayEquals(((List) decode).toArray(), Arrays.stream(str.split("[,]")).map(codec::encode).toArray());
    }

    @Test
    public void testDecodeDate() {
        DateTimeCodec codec = new DateTimeCodec("yyyy-MM-dd", Date.class);

        Date data = new Date();

        Object val = codec.encode(data);
        assertEquals(data, val);

        Object date = codec.encode("2019-01-01");

        assertEquals(codec.decode(date), date);

        assertEquals(codec.decode(((Date) date).getTime()), date);

    }
}