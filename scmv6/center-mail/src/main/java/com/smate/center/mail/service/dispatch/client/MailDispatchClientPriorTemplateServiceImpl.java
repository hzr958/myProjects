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
 * 邮件调度->分配客户端->模板优先服务
 * 
 * @author tsz
 *
 */
@Service("mailDispatchClientPriorTemplateServiceImpl")
public class MailDispatchClientPriorTemplateServiceImpl implements MailDispatchClientService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailClientService mailClientService;
  @Resource(name = "mailDispatchClientPriorAccountServiceImpl")
  private MailDispatchClientService mailDispatchClientService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    List<MailClient> priorTemplateClient = mailClientService
        .getClientByTemmlateCode(String.valueOf(mailDispatchInfo.getMailRecord().getMailTemplateCode()));
    // 如果有 就随机分配
    if (CollectionUtils.isNotEmpty(priorTemplateClient)) {
      String clientName = priorTemplateClient.get(ran.nextInt(priorTemplateClient.size())).getClientName();
      mailDispatchInfo.getMailRecord().setMailClient(clientName);
    } else {
      mailDispatchClientService.excute(mailDispatchInfo);
    }
  }

}
