package com.seepine.http.entity;

/**
 * @author Seepine
 */
public interface StreamProgress {
    /**
     * 开始
     *
     * @param contentLength contentLength
     */
    void startBefore(long contentLength);

    /**
     * 开始
     */
    void start();

    /**
     * 进行中
     *
     * @param progressSize 已经进行的大小
     */
    void progress(long progressSize);

    /**
     * 结束
     */
    void finish();
}
