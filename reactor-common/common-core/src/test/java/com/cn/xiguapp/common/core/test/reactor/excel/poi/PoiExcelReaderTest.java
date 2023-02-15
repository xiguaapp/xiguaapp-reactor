package com.cn.xiguapp.common.core.test.reactor.excel.poi;

import com.cn.xiguapp.common.core.excel.ReactorExcel;
import org.junit.Test;
import reactor.test.StepVerifier;

import java.util.LinkedHashMap;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 16:30
 */
public class PoiExcelReaderTest {
    @Test
    public void testXls() {
        ReactorExcel
                .mapReader( "xls")
                .read(PoiExcelReaderTest.class.getResourceAsStream("/test.xls"))
                .as(StepVerifier::create)
                .expectNext(
                        new LinkedHashMap<String, Object>() {{
                            put("id", 1L);
                            put("name", "test");
                        }},
                        new LinkedHashMap<String, Object>() {{
                            put("id", 2L);
                            put("name", "test2");
                        }})
                .verifyComplete();
    }

    @Test
    public void testXlsx() {
        ReactorExcel
                .mapReader( "xlsx")
                .read(PoiExcelReaderTest.class.getResourceAsStream("/test.xlsx"))
                .as(StepVerifier::create)
                .expectNext(
                        new LinkedHashMap<String, Object>() {{
                            put("id", 1L);
                            put("name", "test");
                            put("age", null);
                        }},
                        new LinkedHashMap<String, Object>() {{
                            put("id", 2L);
                            put("name", "test2");
                            put("age", null);
                        }})
                .verifyComplete();
    }
}
