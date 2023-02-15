package com.cn.xiguaapp.msg.service.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.notify.api.template.Template;
import com.cn.xiguapp.common.core.expression.ExpressionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc
 */
@Getter
@Setter
public class DingTalkMessageTemplate implements Template {

    /**
     * 应用ID
     */
    @NotBlank(message = "[agentId]不能为空")
    private String agentId;
    /**
     * 用户ID集合
     */
    private String userIdList;
    /**
     * 部门id
     */
    private String departmentIdList;
    /**
     * 是否发送所有用户
     */
    private boolean toAllUser;

    @NotBlank(message = "[message]不能为空")
    private String message;


    @SneakyThrows
    public BodyInserters.FormInserter<String> createFormInserter(BodyInserters.FormInserter<String> inserter, Map<String, Object> context) {
        inserter.with("agent_id", this.getAgentId())
                .with("to_all_user", String.valueOf(toAllUser))
                .with("msg",this.createMessage(context));
        if (StringUtils.hasText(userIdList)) {
            inserter.with("userid_list", this.createUserIdList(context));
        }
        if (StringUtils.hasText(departmentIdList)) {
            inserter.with("dept_id_list", this.createDepartmentIdList(context));
        }
        return inserter;

    }

    public UriComponentsBuilder createUriParameter(UriComponentsBuilder builder, Map<String,Object> context){
        builder.queryParam("agent_id", this.getAgentId())
                .queryParam("to_all_user", String.valueOf(toAllUser))
                .queryParam("msg",this.createMessage(context));
        if (StringUtils.hasText(userIdList)) {
            builder.queryParam("userid_list", this.createUserIdList(context));
        }
        if (StringUtils.hasText(departmentIdList)) {
            builder.queryParam("dept_id_list", this.createDepartmentIdList(context));
        }
        return builder;
    }

    public String createUserIdList(Map<String,Object> context) {
        if (StringUtils.isEmpty(userIdList)) {
            return userIdList;
        }
        return ExpressionUtils.analytical(userIdList, context,"spel");
    }

    public String createDepartmentIdList(Map<String,Object> context) {
        if (StringUtils.isEmpty(departmentIdList)) {
            return departmentIdList;
        }
        return ExpressionUtils.analytical(departmentIdList, context,"spel");
    }

    public String createMessage(Map<String,Object> context) {
        JSONObject json = new JSONObject();
        json.put("msgtype", "text");
        json.put("text", Collections
                .singletonMap("content",ExpressionUtils
                        .analytical(message, context,"spel")));
        return json.toJSONString();
    }

}
