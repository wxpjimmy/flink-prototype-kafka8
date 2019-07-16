package com.jimmy.flink.sink;

import com.jimmy.tranquility.AdBillingExtensionService;
import com.jimmy.tranquility.TranquilityService;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SinkToDruid extends RichSinkFunction<List<Map<String, String>>> {
    TranquilityService tranquilityService;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ParameterTool params = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        Properties properties = params.getProperties();
        tranquilityService = AdBillingExtensionService.buildService(properties);
    }

    @Override
    public void close() throws Exception {
//         if(scala.collection.JavaConversions._ != n)
        if(tranquilityService != null) {
            tranquilityService.close();
        }
    }

    @Override
    public void invoke(List<Map<String, String>> data, Context context) throws Exception {

    }


}
