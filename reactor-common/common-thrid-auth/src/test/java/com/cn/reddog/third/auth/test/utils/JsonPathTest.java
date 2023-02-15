package com.cn.xiguaapp.third.auth.test.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JsonPath用法测试
 *
 * @author xiguaapp
 */
public class JsonPathTest {

    @Test
    public void jsonPath() {
        List<Map<String, Map<String, Object>>> list = new ArrayList<>();

        Map<String, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> node = new HashMap<>();
        node.put("emailAddress", "xxxx");
        map.put("handle~", node);
        list.add(map);


        Map<String, Object> master = new HashMap<>();
        master.put("elements", list);
        JSONObject emailObj = JSONObject.parseObject(JSON.toJSONString(master));
        Object object = JSONPath.eval(emailObj, "$['elements'][0]['handle~']['emailAddress']");
        Assert.assertEquals("xxxx", object);
    }
}
