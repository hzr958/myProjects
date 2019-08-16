package com.smate.center.job.framework.manager;

import com.smate.center.job.framework.support.JobConf;
import com.smate.center.job.framework.zookeeper.config.ZooKeeperConf;
import com.smate.center.job.framework.zookeeper.queue.Message;
import com.smate.center.job.framework.zookeeper.queue.MessageQueueSerializer;
import com.sun.istack.internal.NotNull;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 节点集群队列管理器，存放各节点的任务、消息队列等，用于生产消息、分配任务给各节点
 *
 * @author Created by houchuanjie
 * @date 2018/06/20 17:52
 */
@Service
public class ClusterQueueManager implements InitializingBean, DisposableBean {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CuratorFramework client;
  /**
   * 存放各节点的消息队列
   */
  private ConcurrentHashMap<String, DistributedQueue<Message<String>>> nodesMessageQueueMap;

  /**
   * 创建对应节点的消息队列（无消费者），用于生产消息
   *
   * @param nodeName
   */
  public void initMessageQueue(String nodeName) {
    if (!nodesMessageQueueMap.containsKey(nodeName)) {
      synchronized (nodesMessageQueueMap) {
        DistributedQueue<Message<String>> messageDistributedQueue = QueueBuilder
            .builder(client, null, new MessageQueueSerializer<String>(),
                ZooKeeperConf.getMassageQueuePath(nodeName)).buildQueue();
        nodesMessageQueueMap.put(nodeName, messageDistributedQueue);
        try {
          messageDistributedQueue.start();
        } catch (Exception e) {
          //it's ok, actually calling the start method here is dispensable, because,
          // messageDistributedQueue is producer only and lockPath is unused, at the same time
          // the NodeManager has created the nodePath.
          //logger.error("启动消息队列失败！节点名称：{}", nodeName, e);
        }
      }
    }
  }

  /**
   * 对指定节点消息队列放入消息
   *
   * @param nodeName
   * @param message
   * @return
   */
  public boolean produceMessage(@NotNull String nodeName, @NotNull Message<String> message) {
    DistributedQueue<Message<String>> msgQueue = nodesMessageQueueMap.get(nodeName);
    try {
      msgQueue.put(message);
      return true;
    } catch (Exception e) {
      logger.error("产生消息失败！节点名称：{}, 消息内容：{}", nodeName, message, e);
      return false;
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.info("Initializing {}", getClass().getSimpleName());
    nodesMessageQueueMap = new ConcurrentHashMap<>(JobConf.DEFAULT_INITIAL_CAPACITY);
  }

  @Override
  public void destroy() throws Exception {
    logger.error("Destroying {}", getClass().getSimpleName());
    nodesMessageQueueMap.values().forEach(q -> {
      try {
        q.close();
      } catch (IOException e) {
        // it's ok
      }
    });
  }
}
