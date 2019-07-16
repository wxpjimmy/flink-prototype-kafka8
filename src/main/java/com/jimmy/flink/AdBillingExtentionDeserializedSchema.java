package com.jimmy.flink;

import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import com.jimmy.ad.exchange.thrift.model.BillingExtensions;

import java.io.IOException;

public class AdBillingExtentionDeserializedSchema extends AbstractDeserializationSchema<BillingExtensions> {

    private static ThreadLocal<TDeserializer> tDeserializerThreadLocal = new ThreadLocal<TDeserializer>() {
        protected TDeserializer initialValue() {
            return new TDeserializer(new TCompactProtocol.Factory());
        }
    };

    @Override
    public BillingExtensions deserialize(byte[] bytes) throws IOException {
        TDeserializer deserializer = tDeserializerThreadLocal.get();
        BillingExtensions extension = new BillingExtensions();
        try {
            deserializer.deserialize(extension, bytes);
        } catch (TException e) {
            return null;
        }
        return extension;
    }
}
