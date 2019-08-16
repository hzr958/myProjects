package com.smate.center.job.framework.support;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.zookeeper.queue.CustomDistributedQueue;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 子任务队列创建器
 *
 * @author Created by hcj
 * @date 2018/07/23 11:03
 */
public class TaskletQueueBuilder {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private CuratorFramework client;
  private String queuePath;
  private CustomDistributedQueue<TaskletDTO> queue;

  public TaskletQueueBuilder(CuratorFramework client, String queuePath) {
    this.client = client;
    this.queuePath = queuePath;
    build();
  }

  public CustomDistributedQueue<TaskletDTO> getTaskletQueue() {
    return queue;
  }

  private synchronized void build() {
    if (queue == null) {
      queue = new CustomDistributedQueue<>(client, queuePath);
    }
  }
}
