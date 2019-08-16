package com.smate.center.job.framework.zookeeper.config;

import com.smate.core.base.utils.string.StringUtils;
import java.util.concurrent.CountDownLatch;
import org.springframework.util.Assert;

/**
 * ZooKeeper配置相关常量类
 *
 * @author houchuanjie
 * @date 2018/04/02 10:28
 */
public final class ZKConfig {

  /**
   * 存储领导候选人的节点路径，所有活跃的服务器节点都会加入竞争选举
   */
  public static final String LEADER_PATH = "/leadingCandidates";
  /**
   * 有效服务器列表路径，该路径下子节点是临时的，当服务器节点启动时创建，关闭时销毁，运行时更新服务器节点资源信息
   */
  public static final String SERVER_NODES_PATH = "/activeServers";
  /**
   * 共享计数器的路径，可实现分布式共享序列的生成
   */
  public static final String NODE_COUNTER_PATH = "/counter";
  /**
   * 已分配任务队列节点基础路径，子节点为主调度分配过的任务信息，包括离线任务和在线任务
   */
  public static final String DISTRIBUTED_JOB_PATH = "/distributedJobs";
  /**
   * 超时执行的子任务队列节点基础路径，此目录下存储所有回收的超时未认领、超时回收的子任务。
   */
  public static final String TIMEOUT_TASKLET_QUEUE_PATH = "/timeoutTaskletQueue";
  /**
   * 统计初始值
   */
  public static final int NODE_COUNTER_INITIAL_VALUE = 0;
  /**
   * 存储各服务器节点分发的任务和消息队列
   */
  private static final String NODES_PATH = "/nodeQueues";
  /**
   * 节点分配的子任务队列，一般路径形如： /nodeJobs/${nodeName}/distributedQueue/$(sequence}
   */
  private static final String NODE_DISTRIBUTED_QUEUE_PATH = NODES_PATH + "/%s/distributedQueue";
  /**
   * 节点消息队列 一般路径形如：/nodeJobs/${nodeName}/massageQueue/${sequence}
   */
  private static final String NODE_MESSAGE_QUEUE_PATH = NODES_PATH + "/%s/messageQueue";
  /**
   * 领导权的递减命中计数器，做从服务切换主服务主切换缓冲，3次命中leader，值为0时，切换工作模式为主服务
   */
  public static CountDownLatch leaderCountDownLatch = new CountDownLatch(3);
  /**
   * 作为从服务的递减命中计数器，做主服务切换到从服务的切换缓存，3次命中，值为0时，切换工作模式为从服务
   */
  public static CountDownLatch slaveCountDownLatch = new CountDownLatch(3);

  /**
   * 获取服务器节点对应的需要终止任务消息队列
   *
   * @param nodeName
   * @return
   */
  public static String getMassageQueuePath(String nodeName) {
    Assert.isTrue(StringUtils.isNotBlank(nodeName), "节点名称不能为空！");
    return String.format(NODE_MESSAGE_QUEUE_PATH, nodeName);
  }

  public static String getTaskletQueuePath(String nodeName) {
    Assert.isTrue(StringUtils.isNotBlank(nodeName), "节点名称不能为空！");
    return String.format(NODE_DISTRIBUTED_QUEUE_PATH, nodeName);
  }

}
