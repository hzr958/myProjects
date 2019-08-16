package com.smate.center.job.framework.cluster;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.common.po.OnlineJobPO;
import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.dto.OnlineJobDTO;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.support.TaskletMediator;
import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.service.OnlineJobService;
import com.smate.center.job.framework.support.JobConfig;
import com.smate.center.job.framework.support.LeadershipObservable;
import com.smate.center.job.framework.support.TaskletQueueColleague;
import com.smate.center.job.framework.support.ZKFactoryBean;
import com.smate.center.job.framework.util.BeanUtil;
import com.smate.center.job.framework.util.JobUtil;
import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.queue.CustomDistributedQueue;
import com.smate.center.job.framework.zookeeper.service.ZKClientService;
import com.smate.center.job.framework.zookeeper.support.ZKNode;
import com.sun.istack.internal.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.utils.ZKPaths;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ZK任务分发机制下的任务收集器，定时执行
 *
 * @author Created by hcj
 * @date 2018/07/09 17:34
 */
@Component
@EnableScheduling
@Lazy(false)
public class ZooKeeperTaskletCollector extends AbstractSimpleTaskletCollector implements
    InitializingBean, Observer, Runnable {

  /**
   * ZK客户端服务
   */
  @Autowired
  private ZKClientService zkClientService;
  /**
   * 各节点子任务队列的中介
   */
  @Autowired
  private TaskletMediator taskletMediator;
  @Autowired
  private OnlineJobService onlineJobService;
  @Autowired
  private OfflineJobService offlineJobService;
  /**
   * 最近更新到数据库的时间戳信息
   */
  private long lastUpdateTimestamp;
  /**
   * 超时子任务队列
   */
  private CustomDistributedQueue<TaskletDTO> timeoutTaskletQueue;
  /**
   * 是否可运行
   */
  private boolean runnable = false;

  @Scheduled(initialDelay = 5000, fixedDelay = 500)
  public void execute() {
    logger.debug("{} is executed.", getClass().getSimpleName());
    if (!runnable) {
      return;
    }
    try {
      run();
    } catch (Exception e) {
      logger.error("任务收集器执行出错！", e);
    }
  }

  @Override
  public void run() {
    //回收超时未认领的子任务
    recycle(JobConfig.TASKLET_TIMEOUT, TimeUnit.MINUTES);
    //统计任务进度和状态
    statistic();
  }


  @Override
  public void recycle(long timeout, TimeUnit unit) {
    logger.debug("正在回收超时未认领的任务...");
    List<TaskletDTO> timeoutList = resourceManager.getRecentDiscoveredServerNodes().stream()
        .map(JobServerNodePO::getName)
        //过滤出超时的服务器
        .filter(serverName -> {
          //获取服务器节点任务队列的更新时间戳（ZK节点：${namespace}/nodes/nodeXXX/distributedQueue）
          long timestamp = Optional
              .ofNullable(zkClientService.getData(ZKConfig.getTaskletQueuePath(serverName)))
              .map(String::new).map(Long::valueOf)
              .orElse(Instant.now().toEpochMilli());
          return JobUtil.isTimeout(timestamp, timeout, unit);
        })
        // 映射，获取每个超时的服务器节点所对应未认领的子任务列表（并从对应的队列中移除）
        .map(serverName -> {
          List<TaskletDTO> list = taskletMediator.getColleague(serverName)
              .map(TaskletQueueColleague::getTaskletQueue).map(CustomDistributedQueue::clear)
              .orElseGet(Collections::emptyList);
          if (CollectionUtils.isNotEmpty(list)) {
            logger.warn("发现服务器节点'{}'超时未认领的子任务{}个，已回收！", serverName, list.size());
          }
          return list;
        })
        //过滤掉空集合
        .filter(CollectionUtils::isNotEmpty)
        //将多个list平行映射为一个list的stream流
        .flatMap(List::stream)
        //将stream流转换为list
        .collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(timeoutList)) {
      logger.warn("共计回收超时未认领的任务{}个！", timeoutList.size());
      timeoutTaskletQueue.addAll(timeoutList);
    }
  }

  @Override
  public void statistic() {
    logger.debug("统计任务执行进度中...");
    //操作1：统计、计算；这里使用了并行流处理
    Map<JobTypeEnum, Map<JobStatusEnum, List<BaseJobDTO>>> collect = distributedJobMap.entrySet()
        .parallelStream().filter(entry -> {
          if (Objects.isNull(entry.getValue())) {
            logger.error(
                "严重错误！发现在已分配的任务集合中，存在一个空数据节点，该节点名称为：\"{}\"，请排查此问题！",
                entry.getKey());
            return false;
          }
          return true;
        }).map(Entry::getValue).peek(baseJobDTO -> {
          //跳过执行完毕的任务
          if (isNotComplete(baseJobDTO.getStatus())) {
            //ZK中已分配任务节点目录下对应该任务的path，形如：${namespace}/distributedJobs/xxxxxxxxxxxxxxxxxx
            //在此path节点目录下存放着正在执行的子任务节点信息（TaskletDTO）
            String parentPath = ZKPaths
                .makePath(ZKConfig.DISTRIBUTED_JOB_PATH, baseJobDTO.getId());
            //取出该任务的所有子任务（TaskletDTO）集合
            List<String> children = zkClientService.getChildren(parentPath);
            List<TaskletDTO> taskletDTOList = children.parallelStream()
                .map(child -> ZKPaths.makePath(parentPath, child))
                .map(zkClientService::getData).map(data -> (TaskletDTO) ZKNode.deserialize(data))
                .peek(this::peekTimeoutTasklet).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(taskletDTOList)) {
              switch (baseJobDTO.getJobType()) {
                case OFFLINE:
                  offlineJobStatistic((OfflineJobDTO) baseJobDTO, taskletDTOList);
                  break;
                case ONLINE:
                  onlineJobStatistic((OnlineJobDTO) baseJobDTO, taskletDTOList);
                  break;
                default:
              }
            }
          }
        }).collect(Collectors
            .groupingBy(BaseJobDTO::getJobType, Collectors.groupingBy(BaseJobDTO::getStatus)));
    /*操作2：间隔性的更新到数据库
    在这里你可能会问：为什么操作1要对每个状态进行分组？
    答案是：分组其实也不需要的，但在操作1中使用了并行流处理，最后合并结果的时候为了避免并发及效率问题，
    如果操作1最后结果合并为一个list，那么在操作2中，需要再次对这个list进行分类找出执行完成的任务，
    就会进行再一次的遍历操作，影响性能，效率降低（完全没必要）。
     */
    internalFlush2Database(collect);
  }

  private void internalFlush2Database(
      Map<JobTypeEnum, Map<JobStatusEnum, List<BaseJobDTO>>> collect) {
    if (JobUtil.isTimeout(lastUpdateTimestamp, JobConfig.getUpdateDbInternal(), TimeUnit.SECONDS)) {
      List<String> completedIdList = Collections.synchronizedList(new ArrayList<>());
      //更新在线任务
      Optional.ofNullable(collect.get(JobTypeEnum.ONLINE)).ifPresent(map -> {
        //对每个状态分类的在线任务list进行操作
        map.entrySet().parallelStream()
            //挑出处理完成和失败的任务id
            .peek(entry -> {
              if (isComplete(entry.getKey())) {
                completedIdList.addAll(
                    entry.getValue().parallelStream().map(BaseJobDTO::getId)
                        .collect(Collectors.toList()));
              }
            })
            /*对每一个状态的任务列表更新到数据库*/
            .forEach(entry -> {
              List<OnlineJobPO> list = entry.getValue().parallelStream()
                  .map(baseJobDTO -> BeanUtil.map((OnlineJobDTO) baseJobDTO, OnlineJobPO.class))
                  .collect(Collectors.toList());
              if (entry.getKey() == JobStatusEnum.PROCESSED) {
                onlineJobService.batchUpdateProcessed(list);
              } else {
                onlineJobService.batchUpdate(list);
              }
            });
      });
      //更新离线任务
      Optional.ofNullable(collect.get(JobTypeEnum.OFFLINE)).ifPresent(map -> {
        //对每个状态分类的离线任务list进行操作
        List<OfflineJobPO> offlineJobPOList = map.entrySet().parallelStream()
            //挑出处理完成和失败的任务id
            .peek(entry -> {
              if (isComplete(entry.getKey())) {
                completedIdList.addAll(
                    entry.getValue().parallelStream().map(BaseJobDTO::getId)
                        .collect(Collectors.toList()));
              }
            }).flatMap(entry -> entry.getValue().parallelStream()
                .map(baseJobDTO -> BeanUtil.map((OfflineJobDTO) baseJobDTO, OfflineJobPO.class))
                .collect(Collectors.toList()).stream())
            .collect(Collectors.toList());
        /*任务列表更新到数据库*/
        offlineJobService.batchUpdate(offlineJobPOList);
      });
      //从已分配任务ZK中删除已完成或失败的任务（上面已经更新保存到数据库中）
      completedIdList.parallelStream().forEach(distributedJobMap::remove);
      lastUpdateTimestamp = Instant.now().toEpochMilli();
    }
  }


  /**
   * 统计离线任务分片执行情况
   *
   * @param offlineJobDTO 任务
   * @param list 任务分片列表
   */
  protected void offlineJobStatistic(final OfflineJobDTO offlineJobDTO,
      final List<TaskletDTO> list) {
    String jobId = offlineJobDTO.getId();
    Double oldPercent = offlineJobDTO.getPercent();
    JobStatusEnum oldStatus = offlineJobDTO.getStatus();
    JobStatusEnum newStatus = offlineJobDTO.getStatus();
    //计算执行百分比
    computePercentage(offlineJobDTO, list);
    // 对任务分片按执行状态进行分组
    Map<JobStatusEnum, List<TaskletDTO>> group = list.stream()
        .collect(Collectors.groupingBy(TaskletDTO::getStatus, Collectors.toList()));
    // 成功的数量
    int processedNum = Optional.ofNullable(group.get(JobStatusEnum.PROCESSED)).map(List::size)
        .orElse(0);
    // 失败的数量
    int failedNum = Optional.ofNullable(group.get(JobStatusEnum.FAILED)).map(List::size).orElse(0);
    //正在执行的数量
    int processingNum = Optional.ofNullable(group.get(JobStatusEnum.PROCESSING)).map(List::size)
        .orElse(0);
    // 总分片数
    int totalNum = offlineJobDTO.getThreadCount();
    //等待执行的数量
    int waittingNum = totalNum - processedNum - failedNum - processingNum;

    //计算执行时间
    long epochMilli = Instant.now().minusMillis(offlineJobDTO.getStartTime().getTime())
        .toEpochMilli();
    offlineJobDTO.setElapsedTime(epochMilli);
    // 全部执行成功
    if (processedNum == totalNum) {
      // 任务执行完成，设置任务状态为执行完成
      newStatus = JobStatusEnum.PROCESSED;
      if (oldStatus != newStatus) {
        offlineJobDTO.setPercent(1.00);
        logger.info("离线任务（jobName='{}'，jobId='{}'）执行进度：100.0%，任务已经执行完毕！",
            offlineJobDTO.getJobName(), jobId);
      }
    }//部分执行失败
    else if (failedNum > 0 && (failedNum + processedNum) == totalNum) {
      newStatus = JobStatusEnum.FAILED;
      if (oldStatus != newStatus) {
        StringBuilder sb = new StringBuilder();
        group.get(JobStatusEnum.FAILED).forEach(taskletDTO -> {
          String errMsg = String.format(" [partNo=%s，begin=%s, end=%s, count=%s, errMsg='%s']",
              taskletDTO.getThreadNo(), taskletDTO.getBegin(), taskletDTO.getEnd(),
              taskletDTO.getCount(), taskletDTO.getErrMsg());
          sb.append(errMsg + ";");
        });
        offlineJobDTO.setErrMsg(sb.toString());
        logger.error("离线任务（jobId='{}'）执行失败！错误日志：{}", jobId, sb.toString());
      }
    } else if (waittingNum == totalNum) {
      newStatus = JobStatusEnum.WAITING;
    } else {
      newStatus = JobStatusEnum.PROCESSING;
      //统计前后执行进度不一致，打印日志
      if (!oldPercent.equals(offlineJobDTO.getPercent())) {
        logger.info("离线任务（jobName='{}'，jobId='{}'）执行进度：{}%", offlineJobDTO.getJobName(),
            jobId, String.format("%.1f", offlineJobDTO.getPercent() * 100));
      }
    }
    offlineJobDTO.setStatus(newStatus);
    distributedJobMap.put(offlineJobDTO.getId(), offlineJobDTO);
  }

  /**
   * 统计在线任务执行情况
   */
  protected void onlineJobStatistic(final OnlineJobDTO onlineJobDTO, final List<TaskletDTO> list) {
    TaskletDTO taskletDTO = list.get(0);
    String jobId = onlineJobDTO.getId();
    JobStatusEnum oldStatus = onlineJobDTO.getStatus();
    JobStatusEnum newStatus = taskletDTO.getStatus();
    onlineJobDTO.setStatus(newStatus);
    onlineJobDTO.setErrMsg(taskletDTO.getErrMsg());
    long epochMilli = Instant.now().minusMillis(onlineJobDTO.getStartTime().getTime())
        .toEpochMilli();
    onlineJobDTO.setElapsedTime(epochMilli);
    //更新任务状态信息
    distributedJobMap.put(onlineJobDTO.getId(), onlineJobDTO);

    if (oldStatus != newStatus && newStatus == JobStatusEnum.PROCESSED) {
      logger.info("在线任务（jobId='{}'）执行完毕！", jobId);
    } else if (oldStatus != newStatus && newStatus == JobStatusEnum.FAILED) {
      logger.error("在线任务（jobId='{}'）执行失败！错误日志：{}", jobId, onlineJobDTO.getErrMsg());
    }
  }

  /**
   * 挑选出超时的子任务
   */
  protected void peekTimeoutTasklet(final TaskletDTO taskletDTO) {
    //过滤已经执行完成的子任务
    if (isComplete(taskletDTO.getStatus())) {
      return;
    }
    // 这里利用过滤函数，执行一些额外的操作：对超时执行的任务进行回收
    if (JobConfig.isTimeout(taskletDTO)) {
      // 重设任务分片执行进度
      taskletDTO.setPercent(0.0000);
      taskletDTO.setTimestamp(Instant.now().toEpochMilli());
      //从对应的任务节点下移除，移除成功后放入超时任务队列中
      String jobPath = ZKPaths.makePath(ZKConfig.DISTRIBUTED_JOB_PATH, taskletDTO.getId());
      String taskletPath = ZKPaths
          .makePath(jobPath, taskletDTO.getThreadNo() + "_" + taskletDTO.getAssignedServerName());
      boolean deleted = zkClientService.deleteNode(taskletPath);
      if (deleted) {
        timeoutTaskletQueue.offer(taskletDTO);
        if (taskletDTO.getJobType() == JobTypeEnum.ONLINE) {
          logger.warn("在线任务（jobId='{}'）执行超时，已被回收！任务类型：{}，超时时间：{}分钟", taskletDTO.getId(),
              taskletDTO.getJobType().getDescription(), JobConfig.TASKLET_TIMEOUT);
        }
        if (taskletDTO.getJobType() == JobTypeEnum.OFFLINE) {
          logger.warn(
              "离线任务分片（jobId='{}'，threadNo={}）执行超时，已被回收！任务类型：{}，分片起止：[{}, {}]，超时时间：{}分钟",
              taskletDTO.getId(), taskletDTO.getThreadNo(),
              taskletDTO.getJobType().getDescription(), taskletDTO.getBegin(), taskletDTO.getEnd(),
              JobConfig.TASKLET_TIMEOUT);
        }
      }
    }
  }

  private boolean isComplete(@NotNull JobStatusEnum status) {
    return status.in(JobStatusEnum.PROCESSED, JobStatusEnum.FAILED);
  }

  private boolean isNotComplete(@NotNull JobStatusEnum status) {
    return !isComplete(status);
  }

  public void setZkClientService(
      ZKClientService zkClientService) {
    this.zkClientService = zkClientService;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.distributedJobMap = ZKFactoryBean.getDistributedJobMap();
    this.timeoutTaskletQueue = ZKFactoryBean.getTimeoutTaskletDistributedQueue();
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof LeadershipObservable) {
      LeadershipObservable leadershipObservable = (LeadershipObservable) o;
      // 有领导权则此服务可运行
      this.runnable = leadershipObservable.hasLeadership();
    }
  }
}
