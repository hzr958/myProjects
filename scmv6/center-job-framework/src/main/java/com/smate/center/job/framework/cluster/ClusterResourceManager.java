package com.smate.center.job.framework.cluster;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.support.MessageMediator;
import com.smate.center.job.framework.support.MessageQueueBuilder;
import com.smate.center.job.framework.support.TaskletMediator;
import com.smate.center.job.framework.support.TaskletQueueBuilder;
import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.center.job.framework.service.JobServerNodeService;
import com.smate.center.job.framework.support.JobConfig;
import com.smate.center.job.framework.support.LeadershipObservable;
import com.smate.center.job.framework.support.MessageQueueColleague;
import com.smate.center.job.framework.support.TaskletQueueColleague;
import com.smate.center.job.framework.util.SpringUtil;
import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.listener.ServerNodesPathChildrenCacheListener;
import com.smate.center.job.framework.zookeeper.queue.CustomDistributedQueue;
import com.smate.center.job.framework.zookeeper.queue.Message;
import com.smate.center.job.framework.zookeeper.service.ZKClientService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 服务器资源管理器实现类，JobMaster请求使用
 *
 * @author houchuanjie
 * @date 2018/04/04 17:05
 */
@Service
@Order(100)
public class ClusterResourceManager implements ClusterResourceManageable, Observer,
    InitializingBean, DisposableBean {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ZKClientService ZKClientService;
  @Autowired
  private JobServerNodeService serverNodeService;
  @Autowired
  private LeadershipObservable leadershipObservable;
  @Autowired
  private MessageMediator messageMediator;
  @Autowired
  private TaskletMediator taskletMediator;

  /**
   * 可用服务器节点集合，serverName作为key
   */
  private ConcurrentMap<String, JobServerNodePO> enabledServerNodeMap = new ConcurrentHashMap<>(16);

  /**
   * 不可用服务器节点集合，serverName作为key
   */
  private ConcurrentMap<String, JobServerNodePO> disabledServerNodeMap = new ConcurrentHashMap<>(
      16);

  /**
   * 可用线程资源
   */
  private AtomicInteger availableThreadSize = new AtomicInteger(0);

  /**
   * 所有服务器节点用于缓存任务队列的剩余可用大小
   */
  private AtomicInteger availableQueueSize = new AtomicInteger(0);

  @Override
  public void updateServerNode(JobServerNodePO serverNode) {
    if (Objects.isNull(serverNode)) {
      return;
    }
    enabledServerNodeMap.put(serverNode.getName(), serverNode);
    recomputeServerResources();
  }

  /**
   * 向可用服务器节点集合中添加，不可添加当前服务器节点。 如果该node已经在不可用集合中，表明这个节点是重新恢复，将从不可用变为可用，添加到集合中
   *
   * @param serverNode 服务器节点
   */
  public void addEnabledServerNode(JobServerNodePO serverNode) {
    logger.info("发现一个可用的服务器节点：{}，已将其添加至可用服务器列表中！", serverNode);
    disabledServerNodeMap.remove(serverNode.getName(), serverNode);
    enabledServerNodeMap.put(serverNode.getName(), serverNode);
    //创建消息队列
    //clusterQueueManager.initMessageQueue(serverNode.getName());
    //创建节点任务队列
    //clusterQueueManager.initTaskletQueue(serverNode.getName());
    //创建消息队列，放入消息中介中存储，消息通知通过中介来相互传递
    boolean exists = JobConfig.getCurrentServerName().equalsIgnoreCase(serverNode.getName())
        || messageMediator.hasColleague(serverNode.getName());
    if (!exists) {
      DistributedQueue<Message<String>> messageQueue = new MessageQueueBuilder(
          ZKClientService.getClient(), ZKConfig.getMassageQueuePath(serverNode.getName()), null)
          .getMessageQueue();
      messageMediator.addColleague(serverNode.getName(),
          new MessageQueueColleague(serverNode.getName(), messageQueue));
    }
    //创建节点子任务队列，放入任务队列中介中存储，分发任务通过中介来传递
    exists = taskletMediator.hasColleague(serverNode.getName());
    if (!exists) {
      CustomDistributedQueue<TaskletDTO> taskletQueue = new TaskletQueueBuilder(
          ZKClientService.getClient(),
          ZKConfig.getTaskletQueuePath(serverNode.getName())).getTaskletQueue();
      taskletMediator.addColleague(serverNode.getName(),
          new TaskletQueueColleague(serverNode.getName(), taskletQueue));
    }
    recomputeServerResources();
  }

  /**
   * 向不可用的服务器节点集合中添加，不可添加当前服务器节点。 如果该node存在于可用集合中，则从可用集合中移除，放入不可用集合。
   *
   * @param serverNode 服务器节点
   */
  public void addDisabledServerNode(JobServerNodePO serverNode) {
    logger.info("发现服务器节点已经不可用：{}，已将其添加至不可用服务器列表中！", serverNode);
    enabledServerNodeMap.remove(serverNode.getName(), serverNode);
    disabledServerNodeMap.put(serverNode.getName(), serverNode);
    if (!JobConfig.getCurrentServerName().equalsIgnoreCase(serverNode.getName())) {
      messageMediator.removeColleague(serverNode.getName());
    }
    recomputeServerResources();
  }

  public JobServerNodePO getServerNode(String serverName) {
    JobServerNodePO serverNodePO = enabledServerNodeMap
        .getOrDefault(serverName, disabledServerNodeMap.get(serverName));
    return serverNodePO;
  }

  @Override
  public int getAvailableThreadSize() {
    return availableThreadSize.get();
  }

  @Override
  public int getAvailableQueueSize() {
    return availableQueueSize.get();
  }

  private boolean isLeaderServerNode(JobServerNodePO serverNode) {
    if (Objects.nonNull(leadershipObservable.getLeader()) && leadershipObservable.getLeader()
        .equals(serverNode)) {
      return true;
    }
    return false;
  }

  /**
   * 重新计算服务器资源
   */
  private void recomputeServerResources() {
    Collection<JobServerNodePO> serverNodes = Optional.ofNullable(enabledServerNodeMap.values())
        .orElseGet(Collections::emptyList);
    int freeThreadSize = serverNodes.stream().mapToInt(JobServerNodePO::getAvailablePoolSize).sum();
    int freeQueueSize = serverNodes.stream().mapToInt(JobServerNodePO::getAvailableQueueSize).sum();
    availableThreadSize.set(freeThreadSize);
    availableQueueSize.set(freeQueueSize);
    logger.debug("当前所有服务器可用线程资源数：{}，剩余队列大小：{}", availableThreadSize.get(),
        availableQueueSize.get());
  }

  /**
   * （观察者的更新方法）当{@code LeadershipObservable}状态改变时，调用此方法
   *
   * @param o 观察的对象，leadershipObservable
   * @param arg 额外的参数，这里没有参数
   */
  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof LeadershipObservable) {
      LeadershipObservable leadershipObservable = (LeadershipObservable) o;
      if (leadershipObservable.hasLeadership()) {
        try {
          List<String> serverNameList = ZKClientService
              .getChildren(ZKConfig.SERVER_NODES_PATH);
          List<JobServerNodePO> serverNodeList = serverNodeService
              .getServerNodeListByNames(serverNameList);
          for (JobServerNodePO serverNode : serverNodeList) {
            enabledServerNodeMap.put(serverNode.getName(), serverNode);
          }
          recomputeServerResources();
        } catch (Exception e) {
          logger.error("获取服务器节点列表失败！", e);
        }
      }
    }
  }

  @Override
  public List<JobServerNodePO> getEnabledServerNodeList() {
    return new ArrayList<>(enabledServerNodeMap.values());
  }

  @Override
  public List<JobServerNodePO> getDisabledServerNodeList() {
    return new ArrayList<>(disabledServerNodeMap.values());
  }

  @Override
  public List<JobServerNodePO> getRecentDiscoveredServerNodes() {
    Collection<JobServerNodePO> enabled = enabledServerNodeMap.values();
    Collection<JobServerNodePO> disabled = disabledServerNodeMap.values();
    List<JobServerNodePO> all = new ArrayList<>(enabled.size() + disabled.size());
    all.addAll(enabled);
    all.addAll(disabled);
    return all;
  }

  @Override
  public Map<String, Integer> getAvailableThreadSizeMap() {
    return enabledServerNodeMap.values().stream()
        .collect(Collectors.toMap(JobServerNodePO::getName, JobServerNodePO::getAvailablePoolSize));
  }

  @Override
  public Map<String, Integer> getAvailableQueueSizeMap() {
    return enabledServerNodeMap.values().stream().collect(
        Collectors.toMap(JobServerNodePO::getName, JobServerNodePO::getAvailableQueueSize));
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.info("Initializing {}", getClass().getSimpleName());
    // 监听其他服务器节点
    ZKClientService.listenPathChildren(ZKConfig.SERVER_NODES_PATH,
        SpringUtil.getBean(ServerNodesPathChildrenCacheListener.class));
  }

  @Override
  public void destroy() throws Exception {
  }
}
