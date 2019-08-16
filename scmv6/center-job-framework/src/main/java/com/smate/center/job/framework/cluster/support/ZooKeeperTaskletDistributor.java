package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.framework.cluster.ClusterResourceManager;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.support.TaskletMediator;
import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.center.job.framework.support.ZKFactoryBean;
import com.smate.center.job.framework.zookeeper.queue.CustomDistributedQueue;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 使用ZooKeeper实现的任务分发器
 *
 * @author Created by hcj
 * @date 2018/07/05 17:57
 */
@Service
public class ZooKeeperTaskletDistributor extends AbstractSimpleTaskletDistributor implements
    InitializingBean {


  //包含各节点子任务队列的中介者，用于分发任务
  @Autowired
  private TaskletMediator taskletMediator;

  @Autowired
  private ClusterResourceManager clusterResourceManager;

  private CustomDistributedQueue<TaskletDTO> timeoutTaskletQueue;

  @Override
  public void distributeTo(String nodeName, List<TaskletDTO> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      JobServerNodePO serverNode = clusterResourceManager.getServerNode(nodeName);
      int successCount = 0;
      for (TaskletDTO taskletDTO : list) {
        taskletDTO.setAssignedServerName(nodeName);
        boolean success = taskletMediator.offer(nodeName, taskletDTO);
        if (!success) {
          logger.error("将子任务分发至节点'{}'失败了！子任务信息：{}", nodeName, taskletDTO);
          timeoutTaskletQueue.offer(taskletDTO);
        } else {
          successCount++;
        }
      }
      //分发任务成功后即刻更新节点的资源情况，以防止此时该服务器出现异常线程阻塞，线程资源更新不及时导致
      // clusterResourceManager记录的资源情况没有及时刷新，主调度一直分发任务的问题。
      Integer availablePoolSize = serverNode.getAvailablePoolSize();
      Integer availableQueueSize = serverNode.getAvailableQueueSize();
      //差值
      int difVal = successCount - availablePoolSize;
      if (difVal >= 0) {
        availableQueueSize -= difVal;
        availablePoolSize = 0;
      } else {
        availablePoolSize = -difVal;
      }
      serverNode.setAvailablePoolSize(availablePoolSize);
      serverNode.setAvailableQueueSize(availableQueueSize);
      clusterResourceManager.updateServerNode(serverNode);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.info("Initializing {}", getClass().getSimpleName());
    this.timeoutTaskletQueue = ZKFactoryBean.getTimeoutTaskletDistributedQueue();
  }
}
