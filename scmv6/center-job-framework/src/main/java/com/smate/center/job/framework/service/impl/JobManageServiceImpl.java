package com.smate.center.job.framework.service.impl;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.exception.CouldNotStopJobException;
import com.smate.center.job.framework.support.MessageMediator;
import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.center.job.framework.service.JobManageService;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.support.LeadershipObservable;
import com.smate.center.job.framework.util.BeanUtil;
import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.queue.Message;
import com.smate.center.job.framework.zookeeper.queue.MessageType;
import com.smate.center.job.framework.zookeeper.service.ZKClientService;
import com.smate.center.job.framework.zookeeper.support.ZKNode;
import com.smate.center.job.web.vo.OfflineJobVO;
import com.smate.core.base.utils.string.StringUtils;
import com.sun.istack.internal.NotNull;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.curator.utils.ZKPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务创建
 */
@Service
public class JobManageServiceImpl implements JobManageService {

  @Autowired
  private OfflineJobService offlineJobService;
  @Autowired
  private ZKClientService ZKClientService;
  @Autowired
  private LeadershipObservable leadershipObservable;
  @Autowired
  private MessageMediator messageMediator;

  @Override
  public OfflineJobVO stopJob(String jobId)
      throws IllegalArgumentException, CouldNotStopJobException {
    if (StringUtils.isBlank(jobId)) {
      throw new IllegalArgumentException("jobId不能为空！");
    }
    //zookeeper中已分配任务id的ZNode路径
    String jobIdPath = ZKPaths.makePath(ZKConfig.DISTRIBUTED_JOB_PATH, jobId);
    OfflineJobDTO offlineJobDTO = ZKNode.deserialize(ZKClientService.getData(jobIdPath));
    //ZK已分配任务数据节点存在
    if (Objects.nonNull(offlineJobDTO)) {
      //已经执行完毕，直接返回即可
      if (offlineJobDTO.getStatus().in(JobStatusEnum.FAILED, JobStatusEnum.PROCESSED)) {
        return BeanUtil.map(offlineJobDTO, OfflineJobVO.class);
      } else {  //没有执行完毕，则发送打断任务执行的消息
        return produceInterruptJobMsg(jobId);
      }
    } else {
      OfflineJobPO offlineJobPO = offlineJobService.getById(jobId);
      //已经执行完毕的话直接返回
      if (offlineJobPO.getStatus().in(JobStatusEnum.FAILED, JobStatusEnum.PROCESSED)) {
        offlineJobPO.setEnable(false);
        offlineJobService.saveOrUpdate(offlineJobPO);
        return BeanUtil.map(offlineJobPO, OfflineJobVO.class);
      } else {
        OfflineJobVO offlineJobVO = new OfflineJobVO();
        offlineJobVO.setId(jobId);
        offlineJobVO.setEnable(false);
        return offlineJobService.updateWithOnlyChanged(offlineJobVO);
      }
    }
  }

  private OfflineJobVO produceInterruptJobMsg(String jobId) {
    //创建停止任务的消息
    Message<String> message = new Message<>(MessageType.INTERRUPT_JOB, jobId);

    //取主调度节点的名称
    String leaderNodeName = Optional.of(leadershipObservable.getLeader())
        .map(JobServerNodePO::getName)
        .orElseThrow(() -> new CouldNotStopJobException("暂时无法停止任务，找不到主调度节点服务器！"));
    //初始化该节点的消息队列
    //clusterQueueManager.initMessageQueue(leaderNodeName);
    //发送消息，通知主调度节点停止任务的执行，此消息只能发送给主调度节点，以此来保证只有主调度节点会收到此类消
    //息，负责主调度的节点收到消息后，再依次通知其他服务节点打断相关子任务
    //clusterQueueManager.produceMessage(leaderNodeName, message);
    messageMediator.postMessage(leaderNodeName, message);
    //zookeeper中已分配任务id的ZNode路径
    String jobIdPath = ZKPaths.makePath(ZKConfig.DISTRIBUTED_JOB_PATH, jobId);
    //查询任务表的任务状态，10秒还没停止则抛出异常
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Boolean> future = executor.submit(() -> {
      boolean stopped;
      do {
        OfflineJobDTO offJobDTO = ZKNode.deserialize(ZKClientService.getData(jobIdPath));
        //先查询ZK中已分配任务的节点数据状态（实时），取不到再去查数据库
        stopped = Optional.ofNullable(offJobDTO).map(OfflineJobDTO::getStatus)
            .map(status -> status.in(JobStatusEnum.WAITING,JobStatusEnum.FAILED, JobStatusEnum.PROCESSED))
            .orElseGet(() -> offlineJobService.isStopped(jobId));
      } while (!stopped);
      return stopped;
    });
    try {
      future.get(10, TimeUnit.SECONDS);
      OfflineJobPO offlineJobPO = offlineJobService.getById(jobId);
      OfflineJobDTO offJobDTO = ZKNode.deserialize(ZKClientService.getData(jobIdPath));
      if (Objects.nonNull(offJobDTO)) {
        BeanUtil.mergeProperties(offJobDTO, offlineJobPO);
      }
      return BeanUtil.map(offlineJobPO, OfflineJobVO.class);
    } catch (Exception e) {
      throw new CouldNotStopJobException("停止任务超时！");
    }
  }

  @Override
  public OfflineJobVO runJob(@NotNull String jobId) {
    OfflineJobVO offlineJobVO = new OfflineJobVO();
    offlineJobVO.setId(jobId);
    offlineJobVO.setEnable(true);
    offlineJobVO.setStatus(JobStatusEnum.UNPROCESS);
    offlineJobVO.setErrMsg("");
    offlineJobVO.setPercent(0.000);
    offlineJobVO = offlineJobService.updateWithOnlyChanged(offlineJobVO);
    return offlineJobVO;
  }
}
