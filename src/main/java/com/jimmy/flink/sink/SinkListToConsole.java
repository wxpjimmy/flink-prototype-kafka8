package com.jimmy.flink.sink;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.List;
import java.util.Map;

public class SinkListToConsole extends RichSinkFunction<List<Map<String, String>>> {

    @Override
    public void invoke(List<Map<String, String>> tuple, Context context) {
        System.out.println("Sink: " + tuple);
    }
}
