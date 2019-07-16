package com.jimmy.flink.kafka;

import org.apache.kafka.common.serialization.Serializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TCompactProtocol;
import com.jimmy.ad.exchange.thrift.model.BillingExtensions;

public class BillingExtensionSerializer implements Serializer<BillingExtensions> {
    private TSerializer serializer = new TSerializer(new TCompactProtocol.Factory());

    @Override
    public byte[] serialize(String s, BillingExtensions billingExtensions) {
        try {
            return serializer.serialize(billingExtensions);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }
}
