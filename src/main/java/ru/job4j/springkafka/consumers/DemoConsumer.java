package ru.job4j.springkafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.job4j.springkafka.services.CounterService;

@Component
public class DemoConsumer {

    private final CounterService counterService;

    @Autowired
    public DemoConsumer(CounterService counterService) {
        this.counterService = counterService;
    }

    @KafkaListener(topics = {"spring-kafka"}, concurrency = "10")
    public void listenTopic(ConsumerRecord<String, String> record) {
        counterService.increment();
    }
}
