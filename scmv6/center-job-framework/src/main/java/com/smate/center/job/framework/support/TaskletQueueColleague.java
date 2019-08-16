package com.smate.center.job.framework.support;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.zookeeper.queue.CustomDistributedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 子任务队列同事
 *
 * @author Created by hcj
 * @date 2018/07/23 10:23
 */
public class TaskletQueueColleague implements Colleague<TaskletDTO> {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private String name;
  private CustomDistributedQueue<TaskletDTO> taskletQueue;

  public TaskletQueueColleague(
      String name, CustomDistributedQueue<TaskletDTO> taskletQueue) {
    this.name = name;
    this.taskletQueue = taskletQueue;
  }

  @Override
  public boolean execute(TaskletDTO taskletDTO) {
    try {
      return taskletQueue.offer(taskletDTO);
    } catch (Exception e) {
      logger.error("向队列中放入子任务失败！节点名称：'{}'，任务信息：{}", taskletDTO);
      return false;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CustomDistributedQueue<TaskletDTO> getTaskletQueue() {
    return taskletQueue;
  }

  public void setTaskletQueue(
      CustomDistributedQueue<TaskletDTO> taskletQueue) {
    this.taskletQueue = taskletQueue;
  }
}
