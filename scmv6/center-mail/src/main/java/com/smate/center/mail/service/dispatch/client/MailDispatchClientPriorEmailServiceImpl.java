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
 * 邮件调度->分配客户端->优先类别优先服务
 * 
 * @author tsz
 *
 */
@Service("mailDispatchClientPriorEmailServiceImpl")
public class MailDispatchClientPriorEmailServiceImpl implements MailDispatchClientService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailClientService mailClientService;
  @Resource(name = "mailDispatchClientNoPriorServiceImpl")
  private MailDispatchClientService mailDispatchClientService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {

    // 通过收件箱 类别 ,取对应 有配置 接收邮件优先 的客户端
    // 如果有 随机分配
    List<MailClient> priorEmailClient =
        mailClientService.getClientByEmail(mailDispatchInfo.getMailRecord().getReceiver());
    if (CollectionUtils.isNotEmpty(priorEmailClient)) {
      String clientName = priorEmailClient.get(ran.nextInt(priorEmailClient.size())).getClientName();
      mailDispatchInfo.getMailRecord().setMailClient(clientName);
    } else {
      mailDispatchClientService.excute(mailDispatchInfo);
    }

  }

}
