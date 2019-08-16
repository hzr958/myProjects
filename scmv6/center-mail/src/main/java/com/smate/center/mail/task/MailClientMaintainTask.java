package com.smate.center.mail.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.cache.MailCacheService;
import com.smate.center.mail.model.MailClient;
import com.smate.center.mail.model.MailClientInfo;
import com.smate.center.mail.service.MailClientService;
import com.smate.center.mail.service.monitor.MailMonitorService;

/**
 * 邮件发送节点维护任务,主要是监控各个节点是否正常
 * 
 * @author tsz
 *
 */
public class MailClientMaintainTask {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailCacheService mailCacheService;
  @Autowired
  private MailClientService mailClientService;
  @Autowired
  private MailMonitorService mailMonitorService;

  private static final String CHECK_CLIENT_EMAIL_MARK = "already_send";

  public void execute() {
    // TODO 这个任务1分钟没处理完就通知管理员
    // long startTime = new Date().getTime();
    // 维护可用客户端
    maintainAvailableClient();
    // 维护不可用客户端
    maintainUnavailableClient();

  }

  /**
   * 维护可用客户端
   */
  private void maintainAvailableClient() {
    List<MailClient> availableMailClient = mailClientService.getAvailableMailClient();
    // 没有可用节点
    if (CollectionUtils.isEmpty(availableMailClient)) {
      logger.info("邮件节点维护任务没有发现可用节点!!");
      String checkMailMsg = mailCacheService.getMailClientCheckMsg();
      if (!CHECK_CLIENT_EMAIL_MARK.equals(checkMailMsg)) {
        // 发送通知邮件
        mailMonitorService.sendMonitorMail("没有可用客户端", "邮件调度服务没有发现可用的邮件发送客户端,请尽快处理!");
        logger.info("邮件发送节点维护任务 发送邮件通知管理员没有可用节点!");
        // 标记为已经发送邮件 1小时过期
        mailCacheService.putMailClientCheckMsg(CHECK_CLIENT_EMAIL_MARK);
      }
    } else {
      logger.info("有可用节点.检查可用节点的更新情况!");
      logger.info(availableMailClient.toString());
      // 有正常节点 去除发送通知邮件标记
      mailCacheService.removeMailClientCheckMsg();
      // 有节点就检查节点更新时间 如果节点更新时间 1分钟内没有变化 就删除这个节点
      Date currentDate = new Date();
      for (MailClient mailClient : availableMailClient) {
        MailClientInfo mailClientInfo = mailCacheService.getMailClientInfo(mailClient.getClientName());
        if (mailClientInfo == null
            || (currentDate.getTime() - mailClientInfo.getLastUpdateTime().getTime()) / (1000 * 30) > 1) {
          // 更新 mailClient 状态 时间 以及备注 标记为不可用
          mailClientService.updateClientUnavailable(mailClient);
          logger.info("没有找到缓存中的客户端信息,标记客户端为不可用" + mailClient.toString());
        }
      }

    }
  }

  /**
   * 维护不可用客户端
   */
  private void maintainUnavailableClient() {
    List<MailClient> unAvailableMailClient = mailClientService.getUnavailableMailClient();
    if (CollectionUtils.isNotEmpty(unAvailableMailClient)) {
      logger.info("有不可用节点.重新检查不可用节点的更新情况!");
      Date currentDate = new Date();
      for (MailClient mailClient : unAvailableMailClient) {
        MailClientInfo mailClientInfo = mailCacheService.getMailClientInfo(mailClient.getClientName());
        if (mailClientInfo != null
            && (currentDate.getTime() - mailClientInfo.getLastUpdateTime().getTime()) / (1000 * 60) <= 1) {
          // 更新 mailClient 状态 时间 以及备注 标记为可用
          mailClientService.updateClientAvailable(mailClient);
          logger.info("重新找到缓存中的客户端信息,标记客户端为可用" + mailClient.toString());
        }
      }
    }
  }
}
