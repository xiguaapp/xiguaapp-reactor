package com.cn.xiguaapp.common.r2dbc.test.param;

import com.cn.xiguaapp.r2dbc.orm.param.Sort;
import org.junit.Assert;
import org.junit.Test;

public class SortTest {

    @Test
    public void test(){
        Sort sort=new Sort();
        sort.setOrder("-- delete ");
        Assert.assertEquals(sort.getOrder(),"asc");
    }

}