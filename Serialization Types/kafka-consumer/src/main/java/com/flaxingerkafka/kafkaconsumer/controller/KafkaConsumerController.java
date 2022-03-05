package com.flaxingerkafka.kafkaconsumer.controller;

import com.flaxingerkafka.kafkaconsumer.kafka.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
public class KafkaConsumerController {

    private final KafkaTemplate<String, Object> template;
    private final String topicName;
    private final int messagesPerRequest;
//    private CountDownLatch latch;

    public KafkaConsumerController(
            final KafkaTemplate<String, Object> template,
            @Value("${flaxinger-kafka.topic-name}") final String topicName,
            @Value("${flaxinger-kafka.messages-per-request}") final int messagesPerRequest){
        this.template = template;
        this.topicName = topicName;
        this.messagesPerRequest = messagesPerRequest;
    }

//    @GetMapping("/hello")
//    public String test() throws Exception {
//        latch = new CountDownLatch(messagesPerRequest);
//        IntStream.range(0, messagesPerRequest)
//                .forEach(i -> this.template.send(topicName, String.valueOf(i), ));
//    }

    @KafkaListener(topics = "test-topic", clientIdPrefix = "json", containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, Message> cr, @Payload Message payload){
        log.info("Logger 1 [JSON] received key {}: Type[{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
//        latch.countDown();
    }

    @KafkaListener(topics = "test-topic", clientIdPrefix = "string", containerFactory = "kafkaListenerStringContainerFactory")
    public void listenAsString(ConsumerRecord<String, Message> cr, @Payload String payload){
        log.info("Logger 2 [String] received key {}: Type[{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
//        latch.countDown();
    }

    @KafkaListener(topics = "test-topic", clientIdPrefix = "byte", containerFactory = "kafkaListenerByteArrayContainerFactory")
    public void listenAsByteArray(ConsumerRecord<String, Message> cr, @Payload byte[] payload){
        log.info("Logger 3 [ByteArray] received key {}: Type[{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
//        latch.countDown();
    }

    private static String typeIdHeader(Headers headers){
        return StreamSupport.stream(headers.spliterator(), false)
                .filter(header -> header.key().equals("__TypeId__"))
                .findFirst().map(header -> new String(header.value())).orElse("N/A");
    }
}
