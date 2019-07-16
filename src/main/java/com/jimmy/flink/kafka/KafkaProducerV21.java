package com.jimmy.flink.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.thrift.TBase;

import java.util.Properties;

public class KafkaProducerV21 {

    public static void main(String[] args) {
        MessageProvider provider = new BillingMessageProvider(10000);
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "com.jimmy.flink.kafka.BillingExtensionSerializer");

        Producer<String, TBase> producer = new KafkaProducer<>(props);
        int total = 0;
        while (provider.hasNext()) {
            Object message = provider.getMessage();
            if(message instanceof TBase) {
                producer.send(new ProducerRecord<String, TBase>("AdBillingExtension", (TBase)message));
                total += 1;

            }
        }
        System.out.println("Send Message count: " + total);
        producer.close();
    }
}
