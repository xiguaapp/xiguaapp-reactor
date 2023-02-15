package com.cn.xiguaapp.common.r2dbc.test;

import com.cn.xiguaapp.r2dbc.orm.convert.MethodReferenceConverter;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

public class StaticMethodReferenceColumnConvertTest {


    @Test
    public void testConvertLambdaColumn() {

        TestClass testClass = new TestClass();


        Assert.assertEquals(MethodReferenceConverter.convertToColumn(TestClass::getName), "name");

        Assert.assertEquals(MethodReferenceConverter.convertToColumn(TestClass::isEnabled), "enabled");

        Assert.assertEquals(MethodReferenceConverter.convertToColumn(testClass::getName), "name");

        Assert.assertEquals(MethodReferenceConverter.convertToColumn(testClass::isEnabled), "enabled");

    }

    @Getter
    @Setter

    public static class TestClass implements Serializable {
        private String name;

        private boolean enabled;
    }

}