package com.imooc.springcloud.biz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * @author 2349
 */
@Slf4j
@EnableBinding(value = {
        Sink.class
        //my_topic.class
})
public class StreamConsumer {

    @StreamListener(Sink.INPUT)
    public void consumer(Object payload) {
        log.info("message consumed successfully, payload={}", payload);
    }
}
