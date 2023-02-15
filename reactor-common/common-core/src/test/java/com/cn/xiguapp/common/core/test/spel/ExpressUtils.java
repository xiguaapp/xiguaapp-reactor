package com.cn.xiguapp.common.core.test.spel;

import com.cn.xiguapp.common.core.expression.ExpressionUtils;
import org.junit.Test;

import java.util.Collections;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 19:54
 */

public class ExpressUtils {
    @Test
    public void test() throws Exception {
        String analytical = ExpressionUtils.analytical("test-${name}-${name}", Collections.singletonMap("name", "test"), "spel");
        System.out.println("-------------====-=-=-==-=-"+analytical);
        String res = ExpressionUtils.analytical("test-${3+2}", Collections.singletonMap("name", "test"), "spel");
        System.out.println("-0-0-0-9999----------------"+res);

        System.out.println(ExpressionUtils.analytical("75187.1-75187.1÷(1+21%+8.5%+21%×8.5%)", "spel"));
    }
}
