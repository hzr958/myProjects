package com.smate.center.mail.service.dispatch;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.exception.NotExistsClientException;
import com.smate.center.mail.model.MailClient;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.MailClientService;

/**
 * 检查客户端 情况服务
 * 
 * @author tsz
 *
 */
public class MailDispatchCheckClientServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailClientService mailClientService;

  /**
   * 检查 是否有可用客户端 如果没有 抛出异常
   */
  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    List<MailClient> mailClient = mailClientService.getAvailableMailClient();
    if (CollectionUtils.isEmpty(mailClient)) {
      logger.error("分配邮件的时候.没有可用客户端!!");
      throw new NotExistsClientException("分配邮件的时候.没有可用客户端!!");
    } else {
      mailDispatchInfo.setMailClientList(mailClient);
    }
  }

}
