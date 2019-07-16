package com.jimmy.flink;

import com.jimmy.flink.kafka.KafkaUtils;
import com.jimmy.flink.sink.SinkToConsole;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import com.jimmy.ad.exchange.thrift.model.BillingExtensions;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FlinkKafkaDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        //env.enableCheckpointing(30000);
        env.setParallelism(16);
        Properties properties = KafkaUtils.getKafkaProperties();
        FlinkKafkaConsumer08<BillingExtensions> consumer = new FlinkKafkaConsumer08<BillingExtensions>("AdBillingExtension", new AdBillingExtentionDeserializedSchema(), properties);
        consumer.setStartFromEarliest();

//        consumer.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<BillingExtensions>() {
//            @Override
//            public long extractAscendingTimestamp(BillingExtensions billingExtensions) {
//                return System.currentTimeMillis() - 100;
//            }
//        });

        DataStreamSource<BillingExtensions> stream = env.addSource(consumer);
        KeySelector<BillingExtensions, Integer> selector = new RandomKeySelector<BillingExtensions>(16);

        DataStream<Map<Integer, Long>> result = stream.rebalance().keyBy(selector)
                .countWindow(100)
                .fold(new HashMap<Integer, Long>(), new FoldFunction<BillingExtensions, Map<Integer, Long>>() {
                    @Override
                    public Map<Integer, Long> fold(Map<Integer, Long> tuple2, BillingExtensions o) throws Exception {
                        if(tuple2.containsKey(o.actionType)) {
                            tuple2.put(o.actionType, tuple2.get(o.actionType) + 1L);
                        } else {
                            tuple2.put(o.actionType, 1L);
                        }
                        return tuple2;
                    }
                });

        result.addSink(new SinkToConsole());

        env.execute();
    }
}
