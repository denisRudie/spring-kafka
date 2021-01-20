package ru.job4j.springkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SpringKafkaApplication {

    public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringKafkaApplication.class, args);

        String topic = "spring-kafka";
        MyProducer producer = new MyProducer(topic);

        new Thread(() -> {
            for (int i = 1; i < 100; i++) {
                try {
                    producer.send(Integer.toString(i), "Hello from MyProducer");
                    TimeUnit.SECONDS.sleep(5);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        MyConsumer consumer = new MyConsumer(topic);
        consumer.consume(record ->
                System.out.println("Key: " + record.key() + ", Value: " + record.value())
        );

        TimeUnit.MINUTES.sleep(10);
        producer.close();
        consumer.close();
    }
}
