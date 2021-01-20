package ru.job4j.springkafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.springkafka.services.CounterService;

import java.util.UUID;

@RestController
public class DemoController {

    private final KafkaTemplate<String, String> template;
    private final CounterService counterService;
    private final String topic = "spring-kafka";

    @Autowired
    public DemoController(KafkaTemplate<String, String> template, CounterService counterService) {
        this.template = template;
        this.counterService = counterService;
    }

    @GetMapping("/send")
    public String sendRandomMessage() {
        template.send(topic, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        return "OK";
    }

    @GetMapping("/counter")
    public String getCounterResult() {
        return Integer.toString(counterService.getCounter());
    }
}
