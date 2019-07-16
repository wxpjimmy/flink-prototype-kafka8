package com.jimmy.flink.kafka;

import java.util.Properties;

public class KafkaUtils {
    public static Properties getKafkaProperties() {
        Properties properties = new Properties();
        // only required for Kafka 0.8
        properties.setProperty("bootstrap.servers", "xxx:9092");
        properties.setProperty("zookeeper.connect", "xxxxxxx");
        properties.setProperty("group.id", "flink-test");
        return properties;
    }
}
