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
 * @since 1.0 16:28
 */
public class CsvReaderDemo {
    @Test
    @SneakyThrows
    public void testWrite() {

        ReactorExcel
                .writer("xlsx")
                .header("id", "ID")
                .header("name", "name").header("a","a")
                .write(Flux.range(0, 10000)
                        .map(i -> new HashMap<String, Object>() {{
                            put("id", i);
                            put("name", "test" + i);
                            put("a",null);
                        }}), new FileOutputStream("./target/test.xlsx"))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();

        Thread.sleep(1000);
    }
}
