package com.midea.support;

public interface RetryPolicy {
    /**
     * 重连
     * @param retryCount
     * @return
     */
    boolean allowRetry(int retryCount);

    /**
     *
     * @param retryCount current retry count
     * @return the time to sleep
     */
    long getSleepTimeMs(int retryCount);
}
