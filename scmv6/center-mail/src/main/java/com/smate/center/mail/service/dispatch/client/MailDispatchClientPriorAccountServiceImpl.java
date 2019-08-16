package com.smate.center.mail.service.dispatch.client;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.mail.model.MailClient;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.MailClientService;

/**
 * 邮件调度->分配客户端->发送账号优先服务
 * 
 * @author tsz
 *
 */
@Service("mailDispatchClientPriorAccountServiceImpl")
public class MailDispatchClientPriorAccountServiceImpl implements MailDispatchClientService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailClientService mailClientService;
  @Resource(name = "mailDispatchClientPriorEmailServiceImpl")
  private MailDispatchClientService mailDispatchClientService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    List<MailClient> priorSenderAccountClient =
        mailClientService.getClientByAccount(mailDispatchInfo.getMailRecord().getSender());
    if (mailDispatchInfo.getMailRecord().getSender() != null && CollectionUtils.isNotEmpty(priorSenderAccountClient)) {
      String clientName = priorSenderAccountClient.get(ran.nextInt(priorSenderAccountClient.size())).getClientName();
      mailDispatchInfo.getMailRecord().setMailClient(clientName);

    } else {
      mailDispatchClientService.excute(mailDispatchInfo);
    }

  }

}
