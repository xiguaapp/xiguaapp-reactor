package com.cn.xiguaapp.msg.service.email.embedded;

import com.cn.xiguaapp.notify.api.template.Template;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailTemplate implements Template {

    private String subject;

    private String text;

    private List<Attachment> attachments;

    private List<String> sendTo;

    @Getter
    @Setter
    public static class Attachment {

        private String name;

        private String location;

    }
}
