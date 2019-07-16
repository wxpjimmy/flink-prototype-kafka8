package com.jimmy.flink.parameter;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkParameterByExectConfig {

    static class Traveler extends RichMapFunction<String, String> {
        private String passengerName;
        private int age;
        private boolean married ;

        @Override
        public void open(Configuration parameters) throws Exception {
            Configuration config = (Configuration) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
            passengerName = config.getString("name", null);
            age = config.getInteger("age", -1);
            married = config.getBoolean("married", false);
        }

        @Override
        public String map(String value) throws Exception {
            return String.format("[%s-%d-%s] %s", passengerName, age, married, value);
        }
    }


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        Configuration config = new Configuration();
        config.setInteger("age", 20);
        config.setString("name", "jimmy");
        config.setBoolean("married", true);

        env.getConfig().setGlobalJobParameters(config);
        DataStreamSource<String> dt = env.fromElements("beijing", "shanghai", "hangzhou", "shenzhen", "guangzhou");

        dt.map(new Traveler()).print();
        env.execute();
    }
}
