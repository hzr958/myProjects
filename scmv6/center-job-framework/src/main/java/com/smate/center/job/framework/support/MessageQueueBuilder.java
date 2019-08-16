package com.smate.center.job.framework.support;

import com.smate.center.job.framework.zookeeper.queue.Message;
import com.smate.center.job.framework.zookeeper.queue.MessageQueueConsumer;
import com.smate.center.job.framework.zookeeper.queue.MessageQueueSerializer;
import com.sun.istack.internal.NotNull;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息队列构建器
 *
 * @author Created by hcj
 * @date 2018/07/19 16:31
 */
public class MessageQueueBuilder {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private CuratorFramework client;
  private String queuePath;
  private MessageQueueConsumer<String> consumer;
  private DistributedQueue<Message<String>> messageQueue;

  public MessageQueueBuilder(@NotNull CuratorFramework client, @NotNull String queuePath,
      MessageQueueConsumer<String> consumer) {
    this.client = client;
    this.queuePath = queuePath;
    this.consumer = consumer;
    build();
  }

  public DistributedQueue<Message<String>> getMessageQueue() {
    return messageQueue;
  }

  private synchronized void build() {
    if (messageQueue == null) {
      messageQueue = QueueBuilder
          .builder(client, consumer, new MessageQueueSerializer<String>(), queuePath).buildQueue();
      try {
        messageQueue.start();
      } catch (Exception e) {
        logger.error("启动消息队列失败！", e);
      }
    }
  }
}
