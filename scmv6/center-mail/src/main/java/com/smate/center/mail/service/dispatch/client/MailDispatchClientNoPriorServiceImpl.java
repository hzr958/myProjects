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
 * 邮件调度->分配客户端->没有配置优先级
 * 
 * @author tsz
 *
 */
@Service("mailDispatchClientNoPriorServiceImpl")
public class MailDispatchClientNoPriorServiceImpl implements MailDispatchClientService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailClientService mailClientService;
  @Resource(name = "mailDispatchClientServiceImpl")
  private MailDispatchClientService mailDispatchClientService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {

    // 随机分配没有配置优先级的客户端
    List<MailClient> noPriorClient = mailClientService.getClientByNoPrior();
    if (CollectionUtils.isNotEmpty(noPriorClient)) {
      String clientName = noPriorClient.get(ran.nextInt(noPriorClient.size())).getClientName();
      mailDispatchInfo.getMailRecord().setMailClient(clientName);
    } else {
      mailDispatchClientService.excute(mailDispatchInfo);
    }

  }

}
