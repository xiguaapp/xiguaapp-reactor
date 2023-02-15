package com.cn.xiguaapp.r2dbc.orm.rdb.test.codec;

import com.cn.xiguaapp.r2dbc.orm.rdb.codec.BlobValueCodec;
import lombok.SneakyThrows;
import org.junit.Test;
import reactor.core.publisher.Mono;

import javax.sql.rowset.serial.SerialBlob;
import java.io.NotSerializableException;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class BlobValueCodecTest {


    @Test
    @SneakyThrows
    public void testSimple() {
        BlobValueCodec codec = new BlobValueCodec();

        assertNull(codec.decode(null));
        assertNull(codec.encode(null));


        Object[] values = {"1", 1, true, 1L, 0.2D};

        for (Object value : values) {
            Object encode = codec.encode(value);
            assertNotNull(encode);
            assertTrue(encode instanceof byte[]);

            Object decode = codec.decode(new SerialBlob(((byte[]) encode)));

            assertEquals(decode, value);
        }

        for (Object value : values) {
            Object decode = codec.decode(io.r2dbc.spi.Blob.from(Mono.just(ByteBuffer.wrap((byte[])codec.encode(value)))));

            assertEquals(decode, value);
        }
    }

    @Test
    public void testByteArr() {
        byte[] arr = {1, 2, 3};
        BlobValueCodec codec = new BlobValueCodec();

        assertArrayEquals(arr, (byte[]) codec.decode(codec.encode(arr)));

    }

    @Test
    public void testError() {
        BlobValueCodec codec = new BlobValueCodec();

        try {
            codec.encode(new Object());
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NotSerializableException);
        }

    }


}