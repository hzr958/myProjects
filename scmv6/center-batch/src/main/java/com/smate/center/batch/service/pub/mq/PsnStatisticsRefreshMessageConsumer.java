package com.smate.center.batch.service.pub.mq;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.psn.PsnStatisticsUpdateService;
import com.smate.center.batch.service.pub.mq.runnable.PsnStatisticsRefreshRunnable;
import com.smate.center.batch.service.pub.mq.runnable.SleepRejectedExecutionHandler;

/**
 * 人员统计信息更新服务. sns
 * 
 * @author lqh
 * 
 */
@Component("psnStatisticsRefreshMessageConsumer")
public class PsnStatisticsRefreshMessageConsumer {

  /**
   * 
   */
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private int MAX_POOL_SIZE = 20;
  private long THEAD_SLEEP_TIME = 100l;
  private int THEAD_LOOP_MAX_NUM = 15;
  @Autowired
  private PsnStatisticsUpdateService psnStatisticsUpdateService;
  // 正在运行的ID
  private Set<Long> runingIds = new HashSet<Long>();
  // 线程池
  private ThreadPoolExecutor threadPoolExecutor =
      new ThreadPoolExecutor(MAX_POOL_SIZE, MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
          new LinkedBlockingQueue<Runnable>(MAX_POOL_SIZE), new SleepRejectedExecutionHandler(MAX_POOL_SIZE, 100l));

  public void receive(PsnStatisticsRefreshMessage message) throws ServiceException {
    Assert.notNull(message, "消息为NULL");
    PsnStatisticsRefreshMessage msg = (PsnStatisticsRefreshMessage) message;
    Long psnId = msg.getPsnId();
    int wait = 0;
    while (true) {
      if (wait > THEAD_LOOP_MAX_NUM) {
        break;
      }
      if (runingIds.contains(psnId)) {
        try {
          // 休眠100毫秒
          wait++;
          Thread.sleep(THEAD_SLEEP_TIME);
        } catch (InterruptedException e) {
          logger.error("睡眠当前线程出现问题！", e);
        }
      } else {
        break;
      }
    }
    runingIds.add(psnId);
    PsnStatisticsRefreshRunnable task =
        new PsnStatisticsRefreshRunnable(psnStatisticsUpdateService, msg, runingIds, THEAD_SLEEP_TIME * wait);
    threadPoolExecutor.execute(task);
  }
}
