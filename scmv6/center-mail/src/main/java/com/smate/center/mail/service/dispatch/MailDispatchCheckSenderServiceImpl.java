package com.smate.center.mail.service.dispatch;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.exception.NotExistsSenderException;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.model.MailSender;
import com.smate.center.mail.service.MailSenderService;

/**
 * 发送账号 检查服务
 * 
 * @author tsz
 *
 */
public class MailDispatchCheckSenderServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailSenderService mailSenderService;

  /**
   * 检查是否有可用发送账号,如果没有 抛出异常
   */
  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    List<MailSender> senderlist = mailSenderService.getAllAvailableSender();
    if (CollectionUtils.isEmpty(senderlist)) {
      logger.error("分配邮件的时候,没有发现可用的发件账号!");
      throw new NotExistsSenderException("分配邮件的时候,没有发现可用的发件账号!");
    } else {
      mailDispatchInfo.setMailSenderList(senderlist);
    }
  }

}
