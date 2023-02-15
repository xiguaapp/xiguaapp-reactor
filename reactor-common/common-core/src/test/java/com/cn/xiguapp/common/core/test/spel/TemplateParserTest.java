package com.cn.xiguapp.common.core.test.spel;

import com.cn.xiguapp.common.core.expression.TemplateParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class TemplateParserTest {


    @Test
    public void test() {

        String result = TemplateParser.parse("${name}/${name}/${name}", Collections.singletonMap("name", "test"));
        System.out.println("ccccc===-----------"+result);
    }

    @Test
    public void testLarge() {
        String str = "";
        for (int i = 0; i < 1000; i++) {
            str += "test-" + i;
        }
        String result = TemplateParser.parse("test-${name}", Collections.singletonMap("name", str));

        System.out.println("c============----"+result);
        Assert.assertEquals(result, "test-" + str);
    }


}