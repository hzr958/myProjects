package com.smate.center.job.framework.cluster;

import java.util.concurrent.TimeUnit;

/**
 * 任务回收器接口
 * 
 * @author houchuanjie
 * @date 2018/04/16 15:11
 */
public interface TaskletCollectable {

    /**
     * 回收所有超时未更新时间戳的服务器节点所分配的任务
     * 
     * @param timeout
     *            超时时间
     * @param unit
     *            时间单位
     */
    void recycle(final long timeout, final TimeUnit unit) throws InterruptedException;

    /**
     * 收集统计任务进度信息
     */
    void statistic() throws InterruptedException;
}
