package com.smate.center.mail.service.dispatch;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.cache.MailCacheService;
import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.exception.InfoLockedException;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.MailBuildInfoService;

/**
 * 邮件调度-检查邮件监控锁定的账号，如果有锁定 记录 就不发送邮件
 * 
 * @author tsz
 *
 */
public class MailDispatchCheckMonitorLockServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailBuildInfoService mailBuildInfoService;
  @Autowired
  private MailCacheService mailCacheService;

  /**
   * 检查psnid 以及接收邮箱是不是被锁定
   */
  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    // 判断收建邮箱是否被锁定
    String receiverAndCode = mailDispatchInfo.getMailRecord().getReceiver()
        + String.valueOf(mailDispatchInfo.getMailRecord().getMailTemplateCode());
    if (StringUtils.isNotBlank(mailCacheService.getMonitorLock(receiverAndCode))) {
      logger.info("邮件调度－〉收件箱被锁定!" + mailDispatchInfo.getMailRecord().getReceiver() + "邮件模板为："
          + mailDispatchInfo.getMailRecord().getMailTemplateCode());
      throw new InfoLockedException("邮件调度－〉收件箱被锁定!" + mailDispatchInfo.getMailRecord().getReceiver() + "邮件模板为："
          + mailDispatchInfo.getMailRecord().getMailTemplateCode());
    }
    // 判断邮件触发的psnId是否被锁定
    MailOriginalData mailOriginalData =
        mailBuildInfoService.getMailOriginalData(mailDispatchInfo.getMailRecord().getMailId());
    String psnIdAndCode =
        mailOriginalData.getSenderPsnId().toString() + String.valueOf(mailOriginalData.getMailTemplateCode());
    if (mailOriginalData.getSenderPsnId() != 0
        && StringUtils.isNotBlank(mailCacheService.getMonitorLock(psnIdAndCode))) {
      logger.info("邮件调度－〉邮件触发账号psnId 被锁定 !" + mailOriginalData.getSenderPsnId() + "邮件模板为："
          + mailOriginalData.getMailTemplateCode());
      throw new InfoLockedException("邮件调度－〉邮件触发账号psnId 被锁定 " + mailOriginalData.getSenderPsnId() + "邮件模板为："
          + mailOriginalData.getMailTemplateCode());
    }
  }

}
