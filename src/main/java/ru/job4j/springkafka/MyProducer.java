package ru.job4j.springkafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MyProducer implements Closeable {

    private final KafkaProducer<String, String> producer = getProducer();
    private final String topic;

    public MyProducer(String topic) {
        this.topic = topic;
    }

    private KafkaProducer<String, String> getProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    public void send(String key, String value) throws ExecutionException, InterruptedException {
        producer.send(new ProducerRecord<>(topic, key, value)).get();
    }

    @Override
    public void close() {
        producer.close();
    }
}
