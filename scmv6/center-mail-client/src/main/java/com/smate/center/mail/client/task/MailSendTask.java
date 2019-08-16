package com.smate.center.mail.client.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.client.service.MailClientSendService;
import com.smate.center.mail.client.service.MailClientSendThreadServiceImpl;
import com.smate.center.mail.client.utils.MailClientUtils;
import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.connector.model.MailInfo;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 邮件发送任务 (可多线程处理)
 * 
 * @author tsz
 *
 */
public class MailSendTask {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private String clientName;
  @Value("${mail_dispatch_gettobesend}")
  private String gettobesend;

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private ThreadPoolTaskExecutor taskExecutor;
  @Autowired
  private MailClientSendService mailClientSendService;

  @SuppressWarnings("unchecked")
  public void execute() {
    try {
      clientName = MailClientUtils.getClientName();
    } catch (Exception e1) {
      logger.error("获取客户端名字出错!!", e1);
      return;
    }
    try {
      List<String> list = restTemplate.postForObject(gettobesend, clientName, List.class);
      list.forEach(info -> {
        try {
          mailHandler(info);
        } catch (Exception e) {
          logger.error("数据读取出错" + info, e);
        }
      });
    } catch (Exception e) {
      logger.error("获取待发送邮件出错! -->" + clientName, e);
    }
  }

  /**
   * 处理邮件发送逻辑
   * 
   * @param mailInfoStr
   * @param count
   */
  private void mailHandler(String mailInfoStr) {
    logger.info("最大线程:" + taskExecutor.getMaxPoolSize() + ",活跃线程:" + taskExecutor.getActiveCount());
    if (taskExecutor.getMaxPoolSize() - taskExecutor.getActiveCount() > 0) {
      taskExecutor.execute(new MailClientSendThreadServiceImpl(mailClientSendService, mailInfoStr));
    } else {
      MailInfo mailInfo = JacksonUtils.jsonObject(mailInfoStr, MailInfo.class);
      mailClientSendService.updateMailStatus(mailInfo.getMailId(), MailSendStatusEnum.STATUS_1, "没有多于的线程重新设置为待发送");

      logger.info("没有多于的线程! 活跃线程数:" + taskExecutor.getActiveCount());
    }
  }

}
