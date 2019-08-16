package com.smate.center.job.framework.cluster;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.framework.cluster.support.JobFactory;
import com.smate.center.job.framework.cluster.support.TaskletDistributable;
import com.smate.center.job.framework.cluster.support.ZooKeeperTaskletDistributor;
import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.dto.TaskletPackageDTO;
import com.smate.center.job.framework.support.LeadershipObservable;
import com.smate.center.job.framework.support.ZKFactoryBean;
import com.smate.center.job.framework.zookeeper.queue.CustomDistributedQueue;
import com.smate.center.job.framework.zookeeper.support.CustomDistributedMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 系统业务相关的简单任务调度管理器
 *
 * @author houchuanjie
 * @date 2017年12月19日 下午2:44:52
 */
@Component
@EnableScheduling
@Lazy(false)
@Order(101)
public class JobMaster implements InitializingBean, DisposableBean, Observer, Runnable {

  private Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 任务工厂
   */
  @Autowired
  private JobFactory jobFactory;

  @Autowired
  private ClusterResourceManageable clusterResourceManager;

  @Resource(type = ZooKeeperTaskletDistributor.class)
  private TaskletDistributable taskletDistributor;

  private CustomDistributedMap<BaseJobDTO> distributedJobMap;
  /**
   * 超时子任务队列
   */
  private CustomDistributedQueue<TaskletDTO> timeoutTaskletQueue;
  /**
   * 是否可运行
   */
  private boolean runnable = false;

  @Scheduled(initialDelay = 5000, fixedDelay = 10)
  public void execute() {
    logger.debug("{} is executed.", getClass().getSimpleName());
    // 如果没有获得领导权，则返回。
    if (!runnable) {
      return;
    }
    try {
      run();
    } catch (Exception e) {
      logger.error("任务调度执行时出错！", e);
      //严重错误，此处应该发邮件通知
    }
  }

  @Override
  public void run() {
    if (logger.isDebugEnabled()) {
      logger.debug("进入任务主调度管理器...");
    }
    // 重新分配超时回收的子任务
    tryRedistributeTimeoutTasklets();
    if (!timeoutTaskletQueue.isEmpty()) {
      return;
    }
    int availableThreadSize = clusterResourceManager.getAvailableThreadSize();
    int availableQueueSize = clusterResourceManager.getAvailableQueueSize();
    logger.debug("正在获取新任务...");
    // 获取新任务
    List<TaskletPackageDTO<? extends BaseJobDTO>> jobPackList = jobFactory
        .produce(availableThreadSize, availableQueueSize);
    if (CollectionUtils.isNotEmpty(jobPackList)) {
      logger.info("当前所有节点可用资源数：{}, 新获取任务数：{}",
          clusterResourceManager.getAvailableThreadSize() + clusterResourceManager
              .getAvailableQueueSize(), jobPackList.size());
      logger.debug("正在分配任务...");
      // 收集所有任务切分后的小任务
      List<TaskletDTO> taskletList = jobPackList.stream().map(TaskletPackageDTO::getTasklets)
          .flatMap(List::stream).collect(Collectors.toList());
      // 将分配的任务加入已分配任务集合
      jobPackList.stream().map(TaskletPackageDTO::getJob).forEach(job -> {
        job.setStatus(JobStatusEnum.DISTRIBUTED);
        job.setStartTime(new Date());
        job.setElapsedTime(0L);
        distributedJobMap.put(job.getId(), job);
      });
      taskletDistributor.distribute(taskletList);
      logger.debug("任务分配完成！");
    } else {
      logger.debug("未获取到新任务！");
    }
  }

  /**
   * 超时任务重新分配执行
   */
  private void tryRedistributeTimeoutTasklets() {
    int availableSize =
        clusterResourceManager.getAvailableThreadSize() + clusterResourceManager
            .getAvailableQueueSize();
    List<TaskletDTO> list = new ArrayList<>();
    while (availableSize-- > 0) {
      TaskletDTO taskletDTO = timeoutTaskletQueue.poll();
      if (Objects.isNull(taskletDTO)) {
        break;
      }
      list.add(taskletDTO);
    }
    if (CollectionUtils.isNotEmpty(list)) {
      taskletDistributor.distribute(list);
      logger.debug("已将{}个超时的子任务重新分发至各服务节点。", list.size());
    }
  }

  /**
   * 恢复任务调度现场
   */
  public void resume() {

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
      // 如果获得了领导权，首先要恢复任务调度现场
      boolean hasLeadership = leadershipObservable.hasLeadership();
      if (hasLeadership) {
        resume();
      }
      // 有领导权则此服务可运行
      this.runnable = leadershipObservable.hasLeadership();
    }
  }

  public void initializeBean() {

  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.info("Initializing {}", getClass().getSimpleName());
    this.distributedJobMap = ZKFactoryBean.getDistributedJobMap();
    this.timeoutTaskletQueue = ZKFactoryBean.getTimeoutTaskletDistributedQueue();
  }

  @Override
  public void destroy() throws Exception {
    logger.info("Shutting down JobMaster");
    this.runnable = false;
  }
}
