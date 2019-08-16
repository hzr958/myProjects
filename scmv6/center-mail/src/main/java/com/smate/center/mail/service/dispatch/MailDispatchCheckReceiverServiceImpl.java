package com.smate.center.mail.service.dispatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.exception.ExistsBlacklistException;
import com.smate.center.mail.exception.NotExistsWhitelistException;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.MailBlacklistService;
import com.smate.center.mail.service.MailWhitelistService;

/**
 * 邮件调度-检查邮件接收者
 * 
 * @author tsz
 *
 */
public class MailDispatchCheckReceiverServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailWhitelistService mailWhitelistService;

  @Autowired
  private MailBlacklistService mailBlacklistService;

  /**
   * 检查是否白名单 或者黑名单
   */
  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    // 检查域名 有些域名 不需要发
    if (mailBlacklistService.isExistsBlacklistHost(mailDispatchInfo.getMailRecord().getReceiver().split("@")[1])) {
      logger.info("接收者邮箱域名存在于黑名单上!" + mailDispatchInfo.getMailRecord().getReceiver());
      throw new ExistsBlacklistException("接收者邮箱域名存在于黑名单上!" + mailDispatchInfo.getMailRecord().getReceiver());
    }
    // 检查黑名单
    if (mailBlacklistService.isExistsBlacklist(mailDispatchInfo.getMailRecord().getReceiver())) {
      logger.info("接收者存在于黑名单上!" + mailDispatchInfo.getMailRecord().getReceiver());
      throw new ExistsBlacklistException("接收者存在于黑名单上!" + mailDispatchInfo.getMailRecord().getReceiver());
    }
    // 如果是开发机,alpha,测试机,uat 检查白名单
    String env = System.getenv("RUN_ENV");
    if (!"run".equals(env) && !mailWhitelistService.isExistsWhitelist(mailDispatchInfo.getMailRecord().getReceiver())) {
      logger.info("接收者不存在于白名单上!" + mailDispatchInfo.getMailRecord().getReceiver());
      throw new NotExistsWhitelistException("接收者不存在于白名单上!" + mailDispatchInfo.getMailRecord().getReceiver());
    }
  }

}
