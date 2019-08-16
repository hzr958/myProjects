package com.smate.center.mail.service.dispatch;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.mail.exception.NotExistsSenderException;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.dispatch.sender.MailDispatchSenderService;

/**
 * 邮件调度 分配发送邮件邮箱
 * 
 * @author tsz
 *
 */
public class MailDispatchDistributeSenderServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "mailDispatchSenderPriorEmailServiceImpl")
  private MailDispatchSenderService mailDispatchSenderService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    // 不绝对且唯一优先
    // 发送账号 优先级别
    // 收件箱 邮箱类别 为第一优先 ,模板优先为第2优先 其实是 客户端优先
    mailDispatchSenderService.excute(mailDispatchInfo);
    if (mailDispatchInfo.getMailRecord().getSender() == null) {
      logger.error("分配发送账号的时候.没有可用发送账号!!");
      throw new NotExistsSenderException("分配发送账号的时候.没有可用发送账号!!");
    }
  }

}
