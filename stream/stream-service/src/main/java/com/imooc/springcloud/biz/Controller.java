package com.imooc.springcloud.biz;

import com.imooc.springcloud.topics.DelayedTopic;
import com.imooc.springcloud.topics.ErrorTopic;
import com.imooc.springcloud.topics.GroupTopic;
import com.imooc.springcloud.topics.MyTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 2349
 */
@RestController
@Slf4j
public class Controller {

    @Autowired
    private MyTopic producer;

    @Autowired
    private DelayedTopic delayedProducer;

    @Autowired
    private GroupTopic groupProducer;

    @Autowired
    private ErrorTopic errorProducer;

    @PostMapping("/send")
    public void sendMsg(@RequestParam("body") String body) {
        producer.output().send(MessageBuilder.withPayload(body).build());
    }

    @PostMapping("/send-group")
    public void sendMsgGroup(@RequestParam("body") String body) {
        groupProducer.output().send(MessageBuilder.withPayload(body).build());
    }

    /**
     * 单机版重试
     * @param body body
     */
    @PostMapping("/send-error")
    public void sendMsgError(@RequestParam("body") String body) {
        MessageBean bean = new MessageBean();
        bean.setPayload(body);
        errorProducer.output().send(MessageBuilder.withPayload(bean).build());
    }

    @PostMapping("/send-dm")
    public void sendMsgDelay(@RequestParam("body") String body, @RequestParam("seconds") Integer seconds) {
        MessageBean messageBean = new MessageBean();
        messageBean.setPayload(body);
        log.info("ready to send delayed message ");
        delayedProducer.output()
                .send(MessageBuilder.withPayload(body)
                        .setHeader("x-delay", 1000 * seconds)
                        .build());
    }
}
