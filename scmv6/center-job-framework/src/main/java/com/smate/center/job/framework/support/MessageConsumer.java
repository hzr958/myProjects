package com.smate.center.job.framework.support;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.framework.cluster.support.JobProvidable;
import com.smate.center.job.framework.manager.NodeManager;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.zookeeper.queue.Message;
import com.smate.center.job.framework.zookeeper.queue.MessageQueueConsumer;
import com.smate.center.job.framework.zookeeper.queue.MessageType;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ZK消息队列的消费者，单消费者模式
 *
 * @author Created by hcj
 * @date 2018/07/19 14:51
 */
@Component
public class MessageConsumer extends MessageQueueConsumer<String> {

  @Autowired
  private NodeManager nodeManager;
  @Autowired
  private JobProvidable jobProvider;
  @Autowired
  private OfflineJobService offlineJobService;
  @Autowired
  private MessageMediator messageMediator;

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
        if (ZKFactoryBean.getDistributedJobMap().containsKey(jobId)) {
          Message<String> newMsg = new Message<String>(MessageType.INTERRUPT_TASKLET, jobId);
          messageMediator.notifyAll(newMsg);
        }

        break;
    }
  }

  public void setNodeManager(NodeManager nodeManager) {
    this.nodeManager = nodeManager;
  }
}
