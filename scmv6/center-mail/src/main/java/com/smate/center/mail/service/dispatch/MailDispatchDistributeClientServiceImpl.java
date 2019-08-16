package com.smate.center.mail.service.dispatch;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.mail.exception.NotExistsClientException;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.dispatch.client.MailDispatchClientService;

/**
 * 邮件调度 给邮件分配客户端 分配顺序 模板优先->发送账号 优先->收件人邮箱类别 优先 ->没有配置优先的客户端->所有客户端
 * 
 * @author tsz
 *
 */
public class MailDispatchDistributeClientServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "mailDispatchClientPriorTemplateServiceImpl")
  private MailDispatchClientService mailDispatchClientService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    // 优先为不绝对且唯一优先
    // 分派客户端 优先级
    // 客户端的优先 配置 模板为第一优先 其次是 发送账号 在然后 是收件箱类别
    // 其中 如果 客户端 比发送账号 先分配 就不存在 优先发送账号 的优先
    mailDispatchClientService.excute(mailDispatchInfo);
    if (mailDispatchInfo.getMailRecord().getMailClient() == null) {
      logger.error("分配客户端的时候.没有可用客户端!!");
      throw new NotExistsClientException("分配客户端的时候.没有可用客户端!!");
    }
  }
}
