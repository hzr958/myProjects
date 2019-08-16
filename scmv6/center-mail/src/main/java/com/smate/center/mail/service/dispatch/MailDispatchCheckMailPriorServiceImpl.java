package com.smate.center.mail.service.dispatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.cache.MailCacheService;
import com.smate.center.mail.exception.LowPriorException;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.model.MailRecord;
import com.smate.center.mail.service.MailClientService;

/**
 * 检查邮件 优先级
 * 
 * @author yhx
 *
 */
public class MailDispatchCheckMailPriorServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailClientService mailClientService;
  @Autowired
  private MailCacheService mailCacheService;

  /**
   * 检查 邮件优先级 低优先级的等待waitTime秒再分配账号
   */
  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    if (!isHighLevel(mailDispatchInfo.getMailRecord())) {
      logger.info("邮件优先级较低，等待分配中!" + mailDispatchInfo.getMailRecord().getPriorLevel());
      throw new LowPriorException("等待分配!!");
    }
  }

  private boolean isHighLevel(MailRecord mailRecord) {
    String priorLevel = mailRecord.getPriorLevel();
    if ("C".equals(priorLevel) || "D".equals(priorLevel)) {
      if ("true".equals(mailCacheService.getWaitDispatch())) {
        return false;
      } else {
        int waitTime = mailClientService.getMailClientWaitTime();
        if (0 != waitTime) {
          mailCacheService.waitDispatch(waitTime);
        }
        return true;
      }
    } else {
      return true;
    }

  }

}
