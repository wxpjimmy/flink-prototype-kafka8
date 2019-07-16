package com.jimmy.flink.kafka;


import java.util.*;
import com.jimmy.ad.exchange.thrift.model.BillingExtensions;
import com.jimmy.ad.exchange.thrift.model.ActionType;

public class BillingMessageProvider implements MessageProvider<BillingExtensions> {
    private int totalNum;
    private List<ActionType> actionsTypes;
    private int cur = 0;
    private Random random;

    public BillingMessageProvider(int size) {
        this.totalNum = size;
        this.actionsTypes = new ArrayList<>();
        this.random = new Random(System.currentTimeMillis());
        initActionTypes();
    }

    void initActionTypes() {
        Arrays.stream(ActionType.values()).forEach(actionType -> actionsTypes.add(actionType));
    }

    @Override
    public BillingExtensions getMessage() {
        int idx = random.nextInt(actionsTypes.size());
        ActionType actionType = actionsTypes.get(idx);
        BillingExtensions extensions = new BillingExtensions();
        extensions.setActionType(actionType.getValue());
        extensions.setImei(UUID.randomUUID().toString());
        this.cur++;
        return extensions;
    }

    @Override
    public boolean hasNext() {
        return this.cur < totalNum;
    }

    @Override
    public void reset() {
        cur = 0;
    }
}
