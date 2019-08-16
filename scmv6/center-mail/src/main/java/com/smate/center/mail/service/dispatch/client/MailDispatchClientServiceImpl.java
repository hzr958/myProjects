package com.smate.center.mail.service.dispatch.client;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.mail.model.MailClient;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.MailClientService;

/**
 * 邮件调度->分配客户端->默认
 * 
 * @author tsz
 *
 */
@Service("mailDispatchClientServiceImpl")
public class MailDispatchClientServiceImpl implements MailDispatchClientService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailClientService mailClientService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    List<MailClient> client = mailClientService.getAvailableMailClient();
    if (CollectionUtils.isNotEmpty(client)) {
      String clientName = client.get(ran.nextInt(client.size())).getClientName();
      mailDispatchInfo.getMailRecord().setMailClient(clientName);
    }
  }

}
