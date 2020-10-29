package com.imooc.springcloud.biz;

import com.imooc.springcloud.topics.DelayedTopic;
import com.imooc.springcloud.topics.ErrorTopic;
import com.imooc.springcloud.topics.GroupTopic;
import com.imooc.springcloud.topics.MyTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 2349
 */
@Slf4j
@EnableBinding(value = {
        Sink.class,
        MyTopic.class,
        GroupTopic.class,
        DelayedTopic.class,
        ErrorTopic.class
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

    @StreamListener(DelayedTopic.INPUT)
    public void consumerDelayTopic(MessageBean messageBean) {
        log.info("delay topic message consumed successfully, payload={}", messageBean.getPayload());
    }

    private AtomicInteger count = new AtomicInteger(1);

    /**
     * 异常重试单机版
     * @param messageBean bean
     */
    @StreamListener(ErrorTopic.INPUT)
    public void consumerErrorTopic(MessageBean messageBean) {
        log.info("are you ok");
        if (count.incrementAndGet() % 3 == 0) {
            log.info("fine thank you and you?");
            count.set(0);
        } else {
            log.error("what's you problem");
            throw new RuntimeException("im not ok");
        }
        log.info("error topic message consumed successfully, payload={}", messageBean.getPayload());
    }
}
