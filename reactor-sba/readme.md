admin监控-8849

if (DingtailkMessage.sendDingTalkMessage("10145bb312db0d617144d599a2432ee9dbbb4641031da9bfb837b1645677203c"
                    ,timeMillis
                    ,DingtalkSign.sign(timeMillis)
                    , DingTalkTextMessage.builder()
                            .content(str.toString())
                            .isAtAll(false)
                            .build())){
                log.info("消息发送成功");
            }else {
                log.error("消息发送失败");
            }