package com.smate.center.job.framework.support;

import com.smate.center.job.framework.zookeeper.queue.Message;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息队列同事
 *
 * @author Created by hcj
 * @date 2018/07/19 17:12
 */
public class MessageQueueColleague implements Colleague<Message<String>> {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private String name;
  private DistributedQueue<Message<String>> messageQueue;

  public MessageQueueColleague(String name,
      DistributedQueue<Message<String>> messageQueue) {
    this.name = name;
    this.messageQueue = messageQueue;
  }

  @Override
  public boolean execute(Message<String> msg) {
    try {
      messageQueue.put(msg);
      return true;
    } catch (Exception e) {
      logger.error("产生消息失败！节点名称：'{}', 消息内容：{}", name, msg, e);
      return false;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DistributedQueue<Message<String>> getMessageQueue() {
    return messageQueue;
  }

  public void setMessageQueue(
      DistributedQueue<Message<String>> messageQueue) {
    this.messageQueue = messageQueue;
  }
}
