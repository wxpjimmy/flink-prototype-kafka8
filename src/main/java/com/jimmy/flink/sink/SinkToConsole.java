package com.jimmy.flink.sink;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.Map;

public class SinkToConsole extends RichSinkFunction<Map<Integer, Long>> {

    @Override
    public void invoke(Map<Integer, Long> tuple, Context context) {
        System.out.println("Sink: " + tuple);
    }
}
