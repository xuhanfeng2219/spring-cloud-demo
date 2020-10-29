package com.imooc.springcloud.topics;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author 2349
 */
public interface RequeueTopic {

    String INPUT = "requeue-consumer";

    String OUTPUT = "requeue-producer";

    /**
     *
     * @return SubscribableChannel channel
     */
    @Input(INPUT)
    SubscribableChannel input();

    /**
     *
     * @return MessageChannel channel
     */
    @Output(OUTPUT)
    MessageChannel output();
}
