/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/19 下午4:46 >
 *
 *     Send: 1125698980@qq.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.sba.server.utils ;

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.sba.server.configuration.*;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/19
 * @desc 钉钉消息发送
 */
@Slf4j
public class DingtailkMessage {
    //检查状态
    public static Function<OapiRobotSendResponse,Boolean> ofCheckIsSuccess= execute->"ok".equalsIgnoreCase(execute.getErrmsg());

    /**
     * 消息发送
     */
    public static BiConsumer<DingTalkClient,OapiRobotSendRequest> send= (client, request)->Mono.fromSupplier(()->{
        try {
            OapiRobotSendResponse execute = client.execute(request);
            if (!ofCheckIsSuccess.apply(execute)){
                log.error("【钉钉机器人】消息发送失败:状态码{},状态描述{}",execute.getErrcode(),execute.getErrmsg());
                Mono.error(new Exception("【钉钉机器人】消息发送失败"));
            }
        } catch (ApiException e) {
            Mono.error(new Exception("【钉钉机器人】消息发送失败:状态描述",e));
        }
        return Mono.empty();
    }).subscribe();

    public static void sendDingTalkMessage(String access_token, Long timestamp , String sign, DefaultDingTalkMessage message){
        DingTalkClient client =
                new DefaultDingTalkClient(String.format
                        ("https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s",access_token,timestamp,sign));
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        //文本消息
        if (message instanceof DingTalkTextMessage){
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text= new OapiRobotSendRequest.Text();
            text.setContent(((DingTalkTextMessage) message).getContent());
            request.setText(text);
            at.setAtMobiles(((DingTalkTextMessage) message).getAtMobiles());
            at.setIsAtAll(((DingTalkTextMessage) message).isAtAll());
            request.setAt(at);
            send.accept(client,request);

        }
        if (message instanceof DingTalkLinkMessage){
            OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
            link.setMessageUrl(((DingTalkLinkMessage) message).getMessageUrl());
            link.setPicUrl(((DingTalkLinkMessage) message).getPicUrl());
            link.setText(((DingTalkLinkMessage) message).getText());
            link.setTitle(((DingTalkLinkMessage) message).getTitle());
            request.setLink(link);
            request.setMsgtype("link");
            send.accept(client,request);

        }
        if (message instanceof DingTalkMarkdownMessage){
            OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
            request.setMsgtype("markdown");
            markdown.setText(((DingTalkMarkdownMessage) message).getText());
            markdown.setTitle(((DingTalkMarkdownMessage) message).getTitle());
            request.setMarkdown(markdown);
            send.accept(client,request);

        }

        if (message instanceof DingTalkActionCardMessage){
            OapiRobotSendRequest.Actioncard actioncard = new OapiRobotSendRequest.Actioncard();
            request.setMsgtype("actionCard");
            actioncard.setBtnOrientation(((DingTalkActionCardMessage) message).getBtnOrientation());
            actioncard.setTitle(((DingTalkActionCardMessage) message).getTitle());
            actioncard.setSingleTitle(((DingTalkActionCardMessage) message).getSingleTitle());
            actioncard.setSingleURL(((DingTalkActionCardMessage) message).getSingleURL());
            actioncard.setText(((DingTalkActionCardMessage) message).getText());
            send.accept(client,request);

        }
    }
}
