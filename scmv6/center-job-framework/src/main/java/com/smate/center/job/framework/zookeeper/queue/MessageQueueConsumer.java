package com.smate.center.job.framework.zookeeper.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;

/**
 * 消息队列消费者抽象基类
 * @param <T> 消息内容类型，{@link Message}
 */
public abstract class MessageQueueConsumer<T> implements QueueConsumer<Message<T>> {

  @Override
  public void stateChanged(CuratorFramework client, ConnectionState newState) {

  }
}
