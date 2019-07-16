package com.jimmy.flink;


import com.jimmy.ad.exchange.thrift.model.BillingExtensions;
import com.jimmy.flink.kafka.KafkaUtils;
import com.jimmy.flink.sink.SinkListToConsole;
import com.jimmy.tranquility.AdBillingExtensionService;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;

import java.io.InputStream;
import java.util.*;

import static org.apache.flink.api.common.typeinfo.Types.STRING;

public class FlinkTranquilityDemo {

    public static void main(String[] args) throws Exception {
        //String configPath="E:\\Projects\\practice\\flink-prototype-kafka8\\src\\main\\resources\\DruidTranquilityDemo.properties"; //way1
        //InputStream propertiesStream = FlinkTranquilityDemo.class.getResourceAsStream("/DruidTranquilityDemo.properties"); //way2
        InputStream propertiesStream = FlinkTranquilityDemo.class.getClassLoader().getResourceAsStream("DruidTranquilityDemo.properties"); //way3
        ParameterTool parameters = ParameterTool.fromPropertiesFile(propertiesStream);

        System.out.println(parameters.getProperties());

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.getConfig().setGlobalJobParameters(parameters);

        env.setParallelism(16);
        Properties properties = KafkaUtils.getKafkaProperties();
        FlinkKafkaConsumer08<BillingExtensions> consumer = new FlinkKafkaConsumer08<BillingExtensions>("AdBillingExtension", new AdBillingExtentionDeserializedSchema(), properties);
        consumer.setStartFromEarliest();

        DataStreamSource<BillingExtensions> stream = env.addSource(consumer);
        KeySelector<Map<String, String>, Integer> selector = new RandomKeySelector<Map<String, String>>(16);

        DataStream<List<Map<String, String>>> result  = stream.rebalance().filter(p-> p!=null && p.isSetTimestamp() && p.isSetAdid()).map(p->
        {
            Map<String, String> res = null;
           // System.out.println(p);
            try {
                res = AdBillingExtensionService.process2(p);
                //System.out.println("parse succeed: " + res);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            return res;
        })
                .returns(Types.MAP(STRING, STRING))
                .keyBy(selector)
                .countWindow(500)
                .fold(new ArrayList<Map<String, String>>(), new FoldFunction<Map<String, String>, List<Map<String, String>>>() {
                    @Override
                    public List<Map<String, String>> fold(List<Map<String, String>> tuple2, Map<String, String> o) throws Exception {
                        if(o != null)
                            tuple2.add(o);
                        return tuple2;
                    }
                });

        result.addSink(new SinkListToConsole());

        env.execute();
    }
}
