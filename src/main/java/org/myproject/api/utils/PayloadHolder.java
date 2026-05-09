package org.myproject.api.utils;

public class PayloadHolder<T> {
    public T payload;

    public PayloadHolder(T payload) {
        this.payload = payload;
    }
}
