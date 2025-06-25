package com.anode.workflow.thread;

import java.io.Closeable;

public class ClosableThreadLocal<T> implements Closeable {
    public ClosableThreadLocal(final T initialValue) {
        this.threadLocal = new ThreadLocal<>();
        this.threadLocal.set(initialValue);
    }

    public ClosableThreadLocal<T> set(final T value) {
        this.threadLocal.set(value);
        return this;
    }

    public T get() {
        return this.threadLocal.get();
    }

    @Override
    public void close() {
        this.threadLocal.remove();
    }

    // Private
    private ThreadLocal<T> threadLocal;
}
