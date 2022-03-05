package com.flaxingerkafka.kafkaproducer.controller;

import com.flaxingerkafka.kafkaproducer.kafka.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@RestController
public class TestController {

    private final KafkaTemplate<String, Object> template;
    private final String topicName;
    private final int messagesPerRequest;
    private CountDownLatch latch;

    public TestController(
            final KafkaTemplate<String, Object> template,
            @Value("${flaxinger-kafka.topic-name}") final String topicName,
            @Value("${flaxinger-kafka.messages-per-request}") final int messagesPerRequest) {
        this.template = template;
        this.topicName = topicName;
        this.messagesPerRequest = messagesPerRequest;
    }

    @GetMapping("/test")
    public String test() throws Exception {

        latch = new CountDownLatch(messagesPerRequest);
        IntStream.range(0, messagesPerRequest)
                .forEach(i -> this.template.send(topicName, String.valueOf(i),
                        new Message("hello kafka", i)));

        // CountDownLatch는 lock과 유사하며 (이 경우에는 작업이 끝날때 까지) Thread blocking을 한다.
        // 현실에서는 쓰지 않지만 예시를 위해 있다고 함.
        latch.await(60, TimeUnit.SECONDS);
        log.info("greeting is sent");
        return "Hello Kafka!";
    }
}
