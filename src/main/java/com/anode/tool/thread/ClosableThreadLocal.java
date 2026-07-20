package com.anode.tool.thread;

import java.io.Closeable;

/**
 * A ThreadLocal wrapper that implements Closeable for easy resource cleanup.
 */
public class ClosableThreadLocal<T> implements Closeable {
	private ThreadLocal<T> threadLocal;

	/**
	 * Constructs a ClosableThreadLocal with an initial value.
	 * @param initialValue the initial value to set
	 */
	public ClosableThreadLocal(final T initialValue) {
		this.threadLocal = new ThreadLocal<>();
		this.threadLocal.set(initialValue);
	}

	/**
	 * Sets the thread-local value.
	 * @param value the value to set
	 * @return this instance for chaining
	 */
	public ClosableThreadLocal<T> set(final T value) {
		this.threadLocal.set(value);
		return this;
	}

	/**
	 * Gets the thread-local value.
	 * @return the thread-local value
	 */
	public T get() {
		return this.threadLocal.get();
	}

	/**
	 * Closes this resource by removing the thread-local value.
	 */
	@Override
	public void close() {
		this.threadLocal.remove();
	}
}
