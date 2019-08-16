package com.smate.center.job.framework.support;

import com.smate.center.job.framework.cluster.JobMaster;
import com.smate.center.job.framework.cluster.ZooKeeperTaskletCollector;
import com.smate.center.job.framework.cluster.ClusterResourceManager;
import com.smate.center.job.framework.po.JobServerNodePO;
import java.util.Objects;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 被观察者，领导权状态标记，当状态改变时，用来通知观察者更新状态。 观察者有两个，一个是JobMaster，另一个是NodeManager
 *
 * @author houchuanjie
 * @date 2018/04/08 14:08
 */
@Component("leadershipObservable")
public class LeadershipObservable extends Observable implements InitializingBean,
    ApplicationContextAware {

  private Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 领导权标记，记录当前服务器节点是否获得领导权
   */
  private AtomicBoolean leadership;
  /**
   * spring上下文
   */
  private ApplicationContext ctx;
  /**
   * 记录主服务器节点
   */
  private AtomicReference<JobServerNodePO> leader;

  /**
   * 设置是否获得领导权
   *
   * @param isLeader
   */
  public void setLeadership(boolean isLeader) {
    boolean changed = false;
    if (Objects.isNull(this.leadership)) {
      this.leadership = new AtomicBoolean(isLeader);
      changed = true;
    } else {
      // 当且仅当要更新的值与当前值相反时，更新值
      changed = this.leadership.compareAndSet(!isLeader, isLeader);
    }

    if (changed) {
      if (this.leadership.get()) {
        logger.info("当前节点ID = {}，已获得领导权，作为主调度服务运行！", leader.get().getId());
      } else {
        logger.info("当前节点ID = {}，未获得领导权，作为从服务节点运行！", leader.get().getId());
      }
      super.setChanged();
      super.notifyObservers();
    }
  }

  /**
   * 是否获得领导权
   *
   * @return
   */
  public boolean hasLeadership() {
    return leadership.get();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    JobMaster jobMaster = ctx.getBean(JobMaster.class);
    ClusterResourceManager resourceManager = ctx.getBean(ClusterResourceManager.class);
    ZooKeeperTaskletCollector collector = ctx.getBean(ZooKeeperTaskletCollector.class);
    leadership = new AtomicBoolean();
    leader = new AtomicReference<>();
    super.addObserver(jobMaster);
    super.addObserver(resourceManager);
    super.addObserver(collector);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.ctx = applicationContext;
  }

  /**
   * 返回获得领导权的主调度服务器节点，如果没有，返回null
   *
   * @return
   */
  public JobServerNodePO getLeader() {
    return leader.get();
  }

  public void setLeader(JobServerNodePO jobServerNodePO) {
    // 不相同时更新
    if (!Objects.equals(jobServerNodePO, leader.get())) {
      this.leader.set(jobServerNodePO);
      logger.info("主调度服务器节点ID = {}", jobServerNodePO.getId());
    }
  }
}
