package com.smate.center.job.framework.manager;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.support.MessageConsumer;
import com.smate.center.job.framework.support.MessageMediator;
import com.smate.center.job.framework.support.MessageQueueBuilder;
import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.center.job.framework.runner.JobRunnable;
import com.smate.center.job.framework.scheduler.TaskletScheduler;
import com.smate.center.job.framework.support.JobConfig;
import com.smate.center.job.framework.support.JobProgressObservable;
import com.smate.center.job.framework.support.MessageQueueColleague;
import com.smate.center.job.framework.support.ZKFactoryBean;
import com.smate.center.job.framework.util.JobUtil;
import com.smate.center.job.framework.util.SpringUtil;
import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.exception.ZooKeeperServiceException;
import com.smate.center.job.framework.zookeeper.queue.CustomDistributedQueue;
import com.smate.center.job.framework.zookeeper.queue.Message;
import com.smate.center.job.framework.zookeeper.service.ZKClientService;
import com.smate.center.job.framework.zookeeper.support.ZKNode;
import com.smate.core.base.utils.string.StringUtils;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 系统业务相关的简单子任务分片调度管理器
 *
 * @author houchuanjie
 * @date 2018年1月29日 下午3:02:30
 */
@Component
@EnableScheduling
@Lazy(false)
@Order(102)
public class NodeManager implements ApplicationContextAware, Observer, InitializingBean,
    DisposableBean {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private TaskletScheduler taskletScheduler;
  private ApplicationContext ctx;
  @Autowired
  private ZKClientService zkClientService;
  @Autowired
  private MessageMediator messageMediator;
  private CustomDistributedQueue<TaskletDTO> taskletDistributedQueue;
  private int queueCapacity = JobConfig.DEFAULT_QUEUE_CAPACITY;
  private boolean runnable = false;

  @Scheduled(initialDelay = 5000, fixedDelay = 10)
  public void execute() throws SchedulerException {
    if (logger.isDebugEnabled()) {
      logger.debug("{}'s method execute is called.", getClass().getSimpleName());
    }
    if (!runnable) {
      return;
    }
    run();
  }

  public void run() {
    if (logger.isDebugEnabled()) {
      logger.debug("进入节点子任务调度管理器...");
    }
    try {
      loadTasklet();
    } catch (Exception e) {
      logger.error("节点调度执行时出错！", e);
      //严重错误，此处应该发邮件通知
    }
  }

  /**
   * 更新当前服务器节点资源情况
   */
  private void updateResource(int remainingThreadNum, int remainingQueueSize) {
    try {
      JobServerNodePO jobServerNodePO = JobConfig
          .updateAndGetCurrentServerNode(remainingThreadNum, remainingQueueSize);
      String path = ZKPaths.makePath(ZKConfig.SERVER_NODES_PATH, jobServerNodePO.getName());
      zkClientService
          .createOrUpdateNode(CreateMode.EPHEMERAL, path, ZKNode.serialize(jobServerNodePO));
    } catch (ZooKeeperServiceException e) {
      logger.error("更新当前服务器节点ZNode数据信息出错！", e);
    }
  }

  /**
   * 加载子任务
   */
  private void loadTasklet() {
    logger.debug("正在加载子任务...");
    //统计本次获取的子任务数
    int count = 0;
    int availableSize = taskletScheduler.getAvailableThreadSize();
    TaskletDTO nextTasklet = null;
    while (availableSize-- > 0 && (nextTasklet = taskletDistributedQueue.poll()) != null) {
      count++;
      logger.debug("子任务{}：{}", count, nextTasklet);
      Class<? extends JobRunnable> classType = getJobRunnerClass(nextTasklet);
      if (Objects.nonNull(classType)) {
        boolean success = submitJob(nextTasklet, classType);
        //提交子任务不成功，说明线程池满，不在继续获取子任务
        if (!success) {
          taskletDistributedQueue.offer(nextTasklet);
          break;
        }
      }
    }
    int taskletQueueSize = taskletDistributedQueue.size();
    updateResource(availableSize + 1, this.queueCapacity - taskletQueueSize);
    if (count > 0) {
      logger.info("新获取到{}个子任务，已全部提交执行！{}", count,
          taskletQueueSize > 0 ? "队列中还有" + taskletQueueSize + "个子任务等待执行..." : "");
    } else {
      logger.debug("未获取到子任务！");
    }
    /**
     * 取完任务后更新一下节点任务队列的数据（空数据即可），目的是更新该节点的mTime（修改时间戳），主调度通过
     * mTime来判定节点是否超时未领取任务，然后决定是否对该节点分配的子任务进行回收。
     */
    zkClientService.setData(ZKConfig.getTaskletQueuePath(JobConfig.getCurrentServerName()),
        Long.toString(Instant.now().toEpochMilli()));
  }

  /**
   * 提交子任务到子任务执行调度器
   *
   * @param taskletDTO 子任务信息
   */
  private boolean submitJob(TaskletDTO taskletDTO, Class<? extends JobRunnable> classType) {
    if (Objects.isNull(classType)) {
      return false;
    }
    taskletDTO = resetState(taskletDTO);
    // 创建该子任务的被观察者，当前节点管理器作为观察者，用于监测该子任务的状态
    JobProgressObservable jobProgressObservable = new JobProgressObservable(taskletDTO, this);
    // 提交该子任务
    boolean success = taskletScheduler
        .submit(JobUtil.getJobKey(taskletDTO), classType, jobProgressObservable);
    return success;
  }

  /**
   * 获取子任务的执行类，通过子任务的名称{@link TaskletDTO#getJobName()}，在Spring容器中查找该名称的bean类型
   *
   * @param taskletDTO 子任务信息
   * @return 如果能够找到，则返回该子任务的类型，
   */
  @SuppressWarnings("unchecked")
  private Class<? extends JobRunnable> getJobRunnerClass(final TaskletDTO taskletDTO) {
    Class<? extends JobRunnable> classType = null;
    StringBuilder errMsg = new StringBuilder("");
    try {
      classType = (Class<? extends JobRunnable>) ctx.getType(taskletDTO.getJobName());
      if (Objects.isNull(classType)) {
        errMsg.append("创建子任务（jobId=\"").append(taskletDTO.getId()).append("\"）出错！无法确定名称为\"")
            .append(taskletDTO.getJobName()).append("\"的Bean类型，检查Spring注册扫描的bean是否存在同名的情况！");
        logger.error(errMsg.toString());
      }
    } catch (NoSuchBeanDefinitionException e) {
      errMsg.append("创建子任务（jobId=\"").append(taskletDTO.getId()).append("\"）出错！没有找到名称为\"")
          .append(taskletDTO.getJobName()).append("\"的Bean实例，注意子任务执行类必须要有注解@omponent(\"")
          .append(taskletDTO.getJobName()).append("\")！");
      logger.error(errMsg.toString(), e);
      errMsg.append("异常信息：").append(e.toString());
    } catch (ClassCastException e) {
      errMsg.append("创建子任务（jobId=\"").append(taskletDTO.getId()).append("\"）出错！name=\"")
          .append(taskletDTO.getJobName())
          .append("\"的Bean类型不是JobRunnable类型的子类型！子任务执行类必须继承BaseOfflineJobRunner.class类")
          .append("或BaseOnlineJobRunner.class类。");
      logger.error(errMsg.toString(), e);
      errMsg.append("异常信息：").append(e.toString());
    }
    // 子任务执行类的类型找不到
    if (Objects.isNull(classType)) {
      taskletDTO.setStatus(JobStatusEnum.FAILED);
      taskletDTO.setErrMsg(errMsg.toString());
    }
    return classType;
  }

  /**
   * 重置子任务状态
   */
  private TaskletDTO resetState(final TaskletDTO taskletDTO) {
    // 重设执行百分比和偏移量，以防止子任务分片重新执行时进度统计错误。
    taskletDTO.setOffset(0L);
    taskletDTO.setPercent(0.0000);
    // 重置子任务状态为等待中
    taskletDTO.setStatus(JobStatusEnum.WAITING);
    String parent = ZKPaths
        .makePath(ZKConfig.DISTRIBUTED_JOB_PATH, taskletDTO.getId());
    String path = ZKPaths
        .makePath(parent, taskletDTO.getThreadNo() + "_" + JobConfig.getCurrentServerName());
    zkClientService.createOrUpdateNode(CreateMode.PERSISTENT, path, ZKNode.serialize(taskletDTO));
    return taskletDTO;
  }

  /**
   * （观察者的更新方法）{@link JobProgressObservable}状态改变时，调用此方法
   *
   * @param o 观察的对象，leadershipObservable
   * @param arg 额外的参数
   */
  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof JobProgressObservable) {
      JobProgressObservable jobProgressObservable = (JobProgressObservable) o;
      TaskletDTO taskletDTO = jobProgressObservable.getTaskletDTO();
      updateState(taskletDTO);
      return;
    }
  }

  /**
   * 更新子任务状态信息，同步至ZooKeeper节点
   *
   * @param taskletDTO 子任务信息对象
   */
  public void updateState(final TaskletDTO taskletDTO) {
    // 更新时间戳
    taskletDTO.setTimestamp(Instant.now().toEpochMilli());
    // 离线子任务统计进度百分比
    if (taskletDTO.getJobType() == JobTypeEnum.OFFLINE) {
      BigDecimal offset = new BigDecimal(taskletDTO.getOffset());
      BigDecimal count = new BigDecimal(taskletDTO.getCount());
      BigDecimal percent = offset.divide(count, 5, BigDecimal.ROUND_DOWN);
      taskletDTO.setPercent(percent.doubleValue());
    }
    String parent = ZKPaths
        .makePath(ZKConfig.DISTRIBUTED_JOB_PATH, taskletDTO.getId());
    String path = ZKPaths
        .makePath(parent, taskletDTO.getThreadNo() + "_" + JobConfig.getCurrentServerName());

    boolean exists = zkClientService.isExist(path);
    if (exists) {
      zkClientService.setData(path, ZKNode.serialize(taskletDTO));
    }
    //TaskletDTO obj = ZKNode.deserialize(zkClientService.getData(path));
    //System.err.println("任务分片" + obj.getThreadNo() + "的时间戳：" + new Date(obj.getTimestamp()));
  }

  /**
   * 终止对应任务id的所有任务分片
   */
  public void interruptJob(String jobId) {
    //先从已分配的任务队列中取对应任务进行处理
    taskletDistributedQueue.toList().stream()
        .filter(zkNode -> zkNode.getDataObject().getId().equals(jobId)).findFirst()
        .ifPresent(zkNode -> {
          taskletDistributedQueue.remove(zkNode);
          TaskletDTO interruptedTasklet = zkNode.getDataObject();
          interruptedTasklet.setStatus(JobStatusEnum.FAILED);
          interruptedTasklet.setErrMsg("子任务被人为操作终止执行！");
          //重置子任务状态
          updateState(interruptedTasklet);
          logger.warn("子任务被人为操作终止执行！jobId='{}'，jobName='{}', jobType={}", jobId,
              interruptedTasklet.getJobName(), interruptedTasklet.getJobType().getDescription());
        });
    //已提交的任务通知中断执行
    List<JobKey> interruptList = new ArrayList<>();
    String parent = ZKPaths.makePath(ZKConfig.DISTRIBUTED_JOB_PATH, jobId);
    List<String> children = zkClientService.getChildren(parent);
    for (String child : children) {
      if (StringUtils.endsWithIgnoreCase(child, "_" + JobConfig.getCurrentServerName())) {
        TaskletDTO taskletDTO = ZKNode
            .deserialize(zkClientService.getData(ZKPaths.makePath(parent, child)));
        interruptList.add(JobUtil.getJobKey(taskletDTO));
      }
    }
    for (JobKey jobKey : interruptList) {
      taskletScheduler.interrupt(jobKey);
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.ctx = applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.info("Initializing {}", getClass().getSimpleName());
    JobServerNodePO currentServerNode = null;
    do {
      currentServerNode = JobConfig.getCurrentServerNode();
    } while (Objects.isNull(currentServerNode));
    if (taskletScheduler == null) {
      taskletScheduler = new TaskletScheduler(currentServerNode);
    }
    this.queueCapacity = currentServerNode.getQueueCapacity();
    String serverName = currentServerNode.getName();
    try {
      String path = ZKPaths.makePath(ZKConfig.SERVER_NODES_PATH, serverName);
      zkClientService
          .createOrUpdateNode(CreateMode.EPHEMERAL, path, ZKNode.serialize(currentServerNode));
    } catch (ZooKeeperServiceException e) {
    }
    logger.info("Initializing the message queue of current node");
    taskletDistributedQueue = ZKFactoryBean.getTaskletDistributedQueue();
    //创建服务节点的消息队列，并将加入消息中介
    MessageConsumer messageConsumer = SpringUtil.getBean(MessageConsumer.class);
    MessageQueueBuilder messageQueueBuilder = new MessageQueueBuilder(zkClientService.getClient(),
        ZKConfig.getMassageQueuePath(serverName), messageConsumer);
    DistributedQueue<Message<String>> messageQueue = messageQueueBuilder.getMessageQueue();
    messageMediator.addColleague(serverName, new MessageQueueColleague(serverName, messageQueue));
    runnable = true;
  }

  @Override
  public void destroy() throws Exception {
    logger.info("Shutting down NodeManager");
    this.runnable = false;
  }
}
