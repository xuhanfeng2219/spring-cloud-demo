package com.imooc.springcloud.biz;

import com.imooc.springcloud.topics.GroupTopic;
import com.imooc.springcloud.topics.MyTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * @author 2349
 */
@Slf4j
@EnableBinding(value = {
        Sink.class,
        MyTopic.class,
        GroupTopic.class
        //my_topic.class
})
public class StreamConsumer {

    @StreamListener(Sink.INPUT)
    public void consumer(Object payload) {
        log.info("message consumed successfully, payload={}", payload);
    }

    @StreamListener(MyTopic.INPUT)
    public void consumerMyTopic(Object payload) {
        log.info("my topic message consumed successfully, payload={}", payload);
    }

    @StreamListener(GroupTopic.INPUT)
    public void consumerGroupTopic(Object payload) {
        log.info("group topic message consumed successfully, payload={}", payload);
    }
}
