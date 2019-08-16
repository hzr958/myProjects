package com.smate.center.job.framework.support;

import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.queue.CustomDistributedQueue;
import com.smate.center.job.framework.zookeeper.support.CustomDistributedMap;
import java.util.Objects;
import org.apache.curator.framework.CuratorFramework;

/**
 * 任务需要用到ZooKeeper相关的辅助类，使用此工厂来获取（单例模式）
 *
 * @author Created by hcj
 * @date 2018/07/13 16:16
 */

public class ZKFactoryBean {

  private static CuratorFramework curatorFramework;
  //已分配任务集合，获得领导权的主调度使用的
  private static CustomDistributedMap<BaseJobDTO> distributedJobMap;
  //超时子任务队列，获得领导权的主调度使用的
  private static CustomDistributedQueue<TaskletDTO> timeoutTaskletDistributedQueue;
  //子任务队列，节点管理器使用
  private static CustomDistributedQueue<TaskletDTO> taskletDistributedQueue;

  /**
   * 返回基于ZooKeeper实现的已分配任务集合（单例）
   *
   * @return
   */
  public static CustomDistributedMap<BaseJobDTO> getDistributedJobMap() {
    if (Objects.isNull(distributedJobMap)) {
      initDistributedJobMap();
    }
    return distributedJobMap;
  }

  private static synchronized void initDistributedJobMap() {
    if (Objects.isNull(distributedJobMap)) {
      distributedJobMap = new CustomDistributedMap<>(curatorFramework,
          ZKConfig.DISTRIBUTED_JOB_PATH);
    }
  }

  /**
   * 返回超时子任务队列的实例对象（单例）
   *
   * @return
   */
  public static CustomDistributedQueue<TaskletDTO> getTimeoutTaskletDistributedQueue() {
    if (Objects.isNull(timeoutTaskletDistributedQueue)) {
      initTimeoutTaskletDistributedQueue();
    }
    return timeoutTaskletDistributedQueue;
  }

  private static synchronized void initTimeoutTaskletDistributedQueue() {
    if (Objects.isNull(timeoutTaskletDistributedQueue)) {
      timeoutTaskletDistributedQueue = new CustomDistributedQueue<>(curatorFramework,
          ZKConfig.TIMEOUT_TASKLET_QUEUE_PATH);
    }
  }

  public static CustomDistributedQueue<TaskletDTO> getTaskletDistributedQueue() {
    if (Objects.isNull(taskletDistributedQueue)) {
      initTaskletDistributedQueue();
    }
    return taskletDistributedQueue;
  }

  private static synchronized void initTaskletDistributedQueue() {
    if (Objects.isNull(taskletDistributedQueue)) {
      taskletDistributedQueue = new CustomDistributedQueue<>(curatorFramework,
          ZKConfig.getTaskletQueuePath(JobConfig.getCurrentServerName()));
    }
  }

  public static void setZKClient(CuratorFramework client) {
    ZKFactoryBean.curatorFramework = client;
  }

  public static CuratorFramework getCuratorFramework() {
    return curatorFramework;
  }
}
