package com.jimmy.flink;

import org.apache.flink.api.java.functions.KeySelector;

import java.util.Random;

public class RandomKeySelector<T> implements KeySelector<T, Integer> {
    private Random random = new Random();
    private int partitions = 16;
    public RandomKeySelector(int partitions) {
        this.partitions = partitions;
    }

    @Override
    public Integer getKey(T t) throws Exception {
        return random.nextInt(partitions);
    }
}
