package com.jimmy.flink.kafka;

public interface MessageProvider<T> {
    T getMessage();
    boolean hasNext();
    void reset();
}
