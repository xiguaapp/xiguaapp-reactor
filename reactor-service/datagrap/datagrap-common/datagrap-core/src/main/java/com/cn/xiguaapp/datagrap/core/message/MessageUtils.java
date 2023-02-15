package com.cn.xiguaapp.datagrap.core.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.cn.xiguaapp.datagrap.core.message.MessageFactory.getMessageList;

/***
 * @author xiguaapp
 * @date 2020-02-21
 * @desc 字符串转化
 */
@Slf4j
public class MessageUtils {
    /**
     * 将字符串转换成MessageList
     * @param str
     * @return
     */
    public static MessageList string2ML(Object str) {
        return string2ML(str.toString(),",");
    }

    /**
     * 将字符串转换成MessageList
     * @param str
     * @param delimiter 分隔符
     * @return
     */
    public static MessageList string2ML(Object str, String delimiter) {
        MessageList messageList = getMessageList();
        for(String s:str.toString().split(delimiter)){
            messageList.add(s);

        }
        return messageList;
    }


    /**
     * 转换json字符串为ML对象
     * @param json
     * @return
     */
    public static MessageList json2ML(String json){
        log.debug("【MessageUtils】当前的json串：{}",json);
        List<Map<String, Object>> messageObjects = JSON.parseObject(json.replaceAll("\\\\\"", ""), new TypeReference<List<Map<String, Object>>>() {
        });
        MessageList ml = getMessageList();
        ml.addAll(messageObjects);
        return ml;
    }



}
