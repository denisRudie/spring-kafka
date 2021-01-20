package ru.job4j.springkafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.function.Consumer;

public class MyConsumer implements Closeable {

    private final String topic;
    private final KafkaConsumer<String, String> consumer;
    public MyConsumer(String topic) {
        this.topic = topic;
        this.consumer = getConsumer();
    }

    private KafkaConsumer<String, String> getConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> c = new KafkaConsumer<>(props);
        c.subscribe(Collections.singletonList(this.topic));
        return c;
    }

    public void consume(Consumer<ConsumerRecord<String, String>> consumerRecord) {
        new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    consumerRecord.accept(record);
                }
            }
        }).start();
    }

    @Override
    public void close() {
        consumer.close();
    }
}
