package com.smate.center.job.framework.manager;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.framework.po.JobServerNode;
import com.smate.center.job.framework.service.JobProvidable;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.support.DistributedJobMap;
import com.smate.center.job.framework.support.JobConf;
import com.smate.center.job.framework.zookeeper.config.ZooKeeperConf;
import com.smate.center.job.framework.zookeeper.queue.Message;
import com.smate.center.job.framework.zookeeper.queue.MessageQueueConsumer;
import com.smate.center.job.framework.zookeeper.queue.MessageQueueSerializer;
import com.smate.center.job.framework.zookeeper.queue.MessageType;
import java.util.Optional;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 节点队列管理器
 *
 * @author Created by houchuanjie
 * @date 2018/06/20 14:25
 */
@Service
public class NodeQueueManager {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CuratorFramework client;
  @Autowired
  private NodeManager nodeManager;
  @Autowired
  private ResourceManageable resourceManager;
  @Autowired
  private ClusterQueueManager clusterQueueManager;
  @Autowired
  private OfflineJobService offlineJobService;
  @Autowired
  private JobProvidable jobProvider;

  private DistributedQueue<Message<String>> messageDistributedQueue;

  private DistributedJobMap distributedJobMap;


  public void initMessageQueue() {
    if (messageDistributedQueue == null) {
      synchronized (this) {
        messageDistributedQueue = QueueBuilder
            .builder(client, getMessageQueueConsumer(), new MessageQueueSerializer<String>(),
                ZooKeeperConf.getMassageQueuePath(JobConf.getCurrentServerName())).buildQueue();
        try {
          messageDistributedQueue.start();
        } catch (Exception e) {
          logger.error("启动消息队列失败！", e);
        }
      }
    }
  }

  private MessageQueueConsumer<String> getMessageQueueConsumer() {
    return new MessageQueueConsumer<String>() {
      @Override
      public void consumeMessage(Message<String> message) throws Exception {
        String jobId = message.getContent();
        switch (message.getType()) {
          case INTERRUPT_TASKLET:
            nodeManager.interruptJob(jobId);
            break;
          case INTERRUPT_JOB:

            /**
             * 当消息为此类型时，证明当前节点正是leader，主调度服务JobMaster正在运行。发送消息通知各节点停止相关任务分片
             */
            //首先从JobProvider中移除（此时任务状态为已加载，还未进行分配）
            Optional.ofNullable(jobProvider.removeFromCache(jobId)).map(o -> (OfflineJobPO) o)
                .ifPresent(offlineJobPO -> {
                  offlineJobPO.setStatus(JobStatusEnum.FAILED);
                  offlineJobPO.setEnable(false);
                  offlineJobPO.setErrMsg("任务被人为操作终止！");
                  offlineJobService.saveOrUpdate(offlineJobPO);
                });
            //已分配的任务通知各节点停止执行
            if (distributedJobMap.contains(jobId)) {
              Message<String> newMsg = new Message<String>(MessageType.INTERRUPT_TASKLET, jobId);
              resourceManager.getEnabledServerNodeList().stream().map(JobServerNode::getName)
                  .forEach(nodeName -> {
                    clusterQueueManager.produceMessage(nodeName, newMsg);
                  });
            }

            break;
        }
      }
    };
  }

  public void setDistributedJobMap(DistributedJobMap distributedJobMap) {
    this.distributedJobMap = distributedJobMap;
  }
}
