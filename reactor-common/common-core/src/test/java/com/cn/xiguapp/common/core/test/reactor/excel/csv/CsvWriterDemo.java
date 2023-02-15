package com.cn.xiguapp.common.core.test.reactor.excel.csv;

import com.cn.xiguapp.common.core.excel.ReactorExcel;
import lombok.SneakyThrows;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 16:29
 */
public class CsvWriterDemo {
    @Test
    @SneakyThrows
    public void testWrite() {

        ReactorExcel
                .writer("csv")
                .header("id", "ID")
                .header("name", "name")
                .write(Flux.range(0, 1000)
                        .map(i -> new HashMap<String, Object>() {{
                            put("id", i);
                            put("name", "test" + i);
                        }}), new FileOutputStream("./target/test.csv"))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();

    }
}
