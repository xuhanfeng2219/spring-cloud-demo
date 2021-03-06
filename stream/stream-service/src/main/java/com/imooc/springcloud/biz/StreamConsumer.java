package com.imooc.springcloud.biz;

import com.imooc.springcloud.topics.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

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
        ErrorTopic.class,
        RequeueTopic.class,
        DlqTopic.class,
        FallbackTopic.class
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

    @StreamListener(DlqTopic.INPUT)
    public void consumerDlqTopic(MessageBean messageBean) {
        log.info("are you ok dlq");
        if (count.incrementAndGet() % 3 == 0) {
            log.info("dlq fine thank you and you?");
        } else {
            log.error("what's you problem dlq");
            throw new RuntimeException("im not ok dlq");
        }

        log.info("dlq topic message consumed successfully, payload={}", messageBean.getPayload());
    }

    @StreamListener(RequeueTopic.INPUT)
    public void consumerRequeueTopic(MessageBean messageBean) {
        log.info("are you ok");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            log.error("重试报错！", e);
        }
        throw new RuntimeException("im not ok");
    }

    private AtomicInteger count = new AtomicInteger(1);

    /**
     * 异常重试单机版
     *
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

    @StreamListener(FallbackTopic.INPUT)
    public void consumerFallbackTopic(MessageBean messageBean, @Header("version") String version) {
        log.info("are you ok Fallback");
        if ("1.0".equalsIgnoreCase(version)) {
            log.info("Fallback fine thank you and you?");
        } else if ("2.0".equalsIgnoreCase(version)){
            log.error("what's you problem Fallback");
            throw new RuntimeException("im not ok Fallback");
        } else {
            log.info("fallback version={}", version);
        }

        log.info("Fallback topic message consumed successfully, payload={}", messageBean.getPayload());
    }

    @ServiceActivator(inputChannel = "fallback-topic.fallback-group.errors")
    public void fallback (Message<?> message) {
        log.info("fallback entered ");
    }
}
