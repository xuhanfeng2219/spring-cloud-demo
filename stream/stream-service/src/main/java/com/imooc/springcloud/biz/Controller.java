package com.imooc.springcloud.biz;

import com.imooc.springcloud.topics.*;
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

    @Autowired
    private RequeueTopic requeueProducer;

    @Autowired
    private DlqTopic dlqProducer;

    @Autowired
    private FallbackTopic fallbackProducer;

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
     *
     * @param body body
     */
    @PostMapping("/send-error")
    public void sendMsgError(@RequestParam("body") String body) {
        MessageBean bean = new MessageBean();
        bean.setPayload(body);
        errorProducer.output().send(MessageBuilder.withPayload(bean).build());
    }

    @PostMapping("/send-requeue")
    public void sendMsgRequeue(@RequestParam("body") String body) {
        MessageBean bean = new MessageBean();
        bean.setPayload(body);
        requeueProducer.output().send(MessageBuilder.withPayload(bean).build());
    }

    @PostMapping("/send-dlq")
    public void sendMsgDlq(@RequestParam("body") String body) {
        MessageBean bean = new MessageBean();
        bean.setPayload(body);
        dlqProducer.output().send(MessageBuilder.withPayload(bean).build());
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

    @PostMapping("/send-fallback")
    public void sendMsgFallback(@RequestParam("body") String body,
                                @RequestParam(value = "version", defaultValue = "1.0") String version) {
        MessageBean bean = new MessageBean();
        bean.setPayload(body);
        fallbackProducer.output()
                // place order
                .send(
                        MessageBuilder.withPayload(bean)
                                .setHeader("version", version)
                                .build()
                );
    }
}
