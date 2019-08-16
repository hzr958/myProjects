package com.smate.center.batch.service.pub.mq.runnable;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.service.psn.PsnStatisticsUpdateService;
import com.smate.center.batch.service.pub.mq.PsnStatisticsRefreshMessage;

/**
 * 人员统计信息更新服务.
 * 
 * 
 */
public class PsnStatisticsRefreshRunnable implements Runnable {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PsnStatisticsUpdateService service;
  private PsnStatisticsRefreshMessage message;
  private Set<Long> runingIds;
  private long sleepTime = 1000l;

  public PsnStatisticsRefreshRunnable(PsnStatisticsUpdateService service, PsnStatisticsRefreshMessage message,
      Set<Long> runingIds, long sleptTime) {
    super();
    this.service = service;
    this.message = message;
    this.runingIds = runingIds;
    this.sleepTime = this.sleepTime - sleptTime;
  }

  @Override
  public void run() {
    Long psnId = message.getPsnId();
    try {
      // 怕客户端还未执行完毕，先休眠最多1s再处理
      if (this.sleepTime > 0) {
        try {
          Thread.sleep(this.sleepTime);
        } catch (InterruptedException e) {
          logger.error("人员统计信息更新服务睡眠当前线程出现问题！psnid=" + psnId, e);
        }
      }
      if (message.getPub() == 1) {
        service.updatePsnStatisticsByPub(psnId);
      }
      if (message.getPrj() == 1) {
        service.updatePsnStatisticsByPrj(psnId);
      }
      if (message.getGroup() == 1) {
        service.updatePsnStatisticsByGroup(psnId);
      }
      if (message.getFriend() == 1) {
        service.updatePsnStatisticsByFrd(psnId);
      }
      if (message.getPubaward() == 1) {
        service.updatePsnStatisticsByPubAward(psnId);
      }

    } catch (Exception e) {
      logger.error("人员统计信息更新服务失败，psnid=" + psnId, e);
    } finally {
      runingIds.remove(psnId);
    }
  }

}
