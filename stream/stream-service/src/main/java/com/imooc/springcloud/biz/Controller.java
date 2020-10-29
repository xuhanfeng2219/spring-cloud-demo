package com.imooc.springcloud.biz;

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
    private GroupTopic groupProducer;

    @PostMapping("/send")
    public void sendMsg(@RequestParam("body") String body) {
        producer.output().send(MessageBuilder.withPayload(body).build());
    }

    @PostMapping("/send-group")
    public void sendMsgGroup(@RequestParam("body") String body) {
        groupProducer.output().send(MessageBuilder.withPayload(body).build());
    }
}
