package com.smate.center.batch.service.pub.mq.runnable;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务队列满了，睡眠策略; queueSize 等待任务执行的队列最大数（默认10） sleepTime 休眠时间（默认50毫秒）
 * 
 * 
 */
public class SleepRejectedExecutionHandler implements RejectedExecutionHandler {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private Integer queueSize = 20;
  private Long sleepTime = 50l;

  public SleepRejectedExecutionHandler() {
    super();
  }

  public SleepRejectedExecutionHandler(Integer queueSize, Long sleepTime) {
    super();
    this.queueSize = queueSize;
    this.sleepTime = sleepTime;
  }

  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    while (true) {
      if (executor.getQueue().size() >= queueSize) {
        try {
          // 休眠
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          logger.error("睡眠当前线程出现问题！", e);
        }
      } else {
        break;
      }
    }
    executor.execute(r);
  }

}
