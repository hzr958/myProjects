package com.smate.center.job.framework.support;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.center.job.framework.service.JobServerNodeService;
import com.smate.center.job.framework.util.JobUtil;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 运行时公用的任务子任务配置信息类
 *
 * @author houchuanjie
 * @date 2018/04/03 16:30
 */
public final class JobConfig implements InitializingBean {

  /**
   * 默认超时回收时间：5分钟
   */
  public static final long DEFAULT_TASKLET_TIMEOUT = 5;
  /**
   * 队列大小，约定计算公式：队列大小 = 从服务器执行任务的线程池最大线程数 * 从服务器数量 * 2倍 * 比例因子（默认75%） + 1；
   */
  public static final int queueInitialCapacity = 200 * 5 * 2 * 3 / 4 + 1;
  /**
   * 默认队列大小
   */
  public static final int DEFAULT_INITIAL_CAPACITY = 16;
  /**
   * 线程池默认核心线程数
   */
  public static final int DEFAULT_CORE_POOL_SIZE = 10;
  /**
   * 线程池默认最大线程数
   */
  public static final int DEFAULT_MAX_POOL_SIZE = 400;
  /**
   * 子任务队列容量大小
   */
  public static final int DEFAULT_QUEUE_CAPACITY = 200;
  /**
   * 线程池默认线程保持活跃的时间，单位：ms
   */
  public static final int DEFAULT_KEEP_ALIVE_SECONDS = 5000;
  /**
   * 默认更新任务信息到数据库的间隔时间，单位：秒
   */
  private static final long DEFAULT_UPDATE_DB_INTERNAL = 1;
  private static final Logger LOGGER = LoggerFactory.getLogger(JobConfig.class);
  /**
   * 当前服务器节点信息
   */
  private static final AtomicReference<JobServerNodePO> currentServerNode = new AtomicReference<JobServerNodePO>();
  /**
   * 缓存超时时间，单位：分钟，用于缓存回收
   */
  public static long TASKLET_TIMEOUT = DEFAULT_TASKLET_TIMEOUT;
  /**
   * 更新任务信息到数据库的间隔时间
   */
  private static long updateDbInternal = DEFAULT_UPDATE_DB_INTERNAL;
  /**
   * 当前服务器节点名称
   */
  private static String currentServerName;
  /**
   * 当前服务器节点IP
   */
  private static String currentServerIP;
  @Autowired
  private JobServerNodeService jobServerNodeService;

  /**
   * 获取当前服务器节点的ID
   *
   * @return
   * @author houchuanjie
   * @date 2018年4月24日 上午10:58:35
   */
  public static Integer getCurrentServerNodeId() {
    return currentServerNode.get().getId();
  }

  /**
   * 获取当前服务器节点信息
   *
   * @return
   */
  public static JobServerNodePO getCurrentServerNode() {
    return currentServerNode.get();
  }

  /**
   * 设置当前服务器节点信息
   *
   * @param node
   */
  public static void setCurrentServerNode(JobServerNodePO node) {
    currentServerNode.set(node);
  }

  public static JobServerNodePO updateAndGetCurrentServerNode(int availablePoolSize,
      int availableQueueSize) {
    return currentServerNode.updateAndGet(serverNode -> {
      serverNode.setAvailablePoolSize(availablePoolSize);
      serverNode.setAvailableQueueSize(availableQueueSize);
      return serverNode;
    });
  }

  /**
   * 判断任务是否执行超时
   *
   * @param taskletDTO
   * @return
   */
  public static boolean isTimeout(TaskletDTO taskletDTO) {
    return JobUtil.isTimeout(taskletDTO.getTimestamp(), TASKLET_TIMEOUT, TimeUnit.MINUTES);
  }

  /**
   * 获取当前服务器节点的名称
   *
   * @return currentServerName
   */
  public static String getCurrentServerName() {
    return currentServerName;
  }

  /**
   * 设置当前服务器节点的名称
   *
   * @param currentServerName 要设置的 currentServerName
   */
  public void setCurrentServerName(String currentServerName) {
    JobConfig.currentServerName = currentServerName;
  }

  /**
   * 获取更新任务信息到数据库的间隔时间，单位：秒
   *
   * @return 间隔时长
   */
  public static long getUpdateDbInternal() {
    return updateDbInternal;
  }

  /**
   * 设置更新任务信息到数据库的间隔时间，单位：秒
   *
   * @param seconds 秒
   */
  public static void setUpdateDbInternal(long seconds) {
    JobConfig.updateDbInternal = seconds;
  }

  /**
   * 获取当前服务器节点的ip地址
   *
   * @return currentServerIP
   */
  public static String getCurrentServerIP() {
    return currentServerIP;
  }

  /**
   * 设置当前服务器节点的ip地址
   *
   * @param currentServerIP 要设置的 currentServerIP
   */
  public void setCurrentServerIP(String currentServerIP) {
    JobConfig.currentServerIP = currentServerIP;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("Initializing {}", getClass().getSimpleName());
    // 获取服务器节点配置信息
    JobServerNodePO serverNode = jobServerNodeService.getServerNode(currentServerName);
    if (Objects.isNull(serverNode)) {
      serverNode = new JobServerNodePO(currentServerName, currentServerIP);
      jobServerNodeService.saveServerNode(serverNode);
    }
    // 设置当前节点配置信息
    currentServerNode.set(serverNode);
  }
}
