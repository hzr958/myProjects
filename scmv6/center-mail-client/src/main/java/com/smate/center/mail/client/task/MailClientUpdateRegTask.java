package com.smate.center.mail.client.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.client.model.MailClientInfo;
import com.smate.center.mail.client.utils.MailClientUtils;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 邮件发送客户端 节点维护信息
 * 
 * @author tsz
 *
 */
public class MailClientUpdateRegTask {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private String clientName;
  @Value("${mail_dispatch_updateclient}")
  private String updateclient;
  @Autowired
  private RestTemplate restTemplate;

  public void execute() {
    try {
      clientName = MailClientUtils.getClientName();
    } catch (Exception e1) {
      logger.error("获取客户端名字出错!!", e1);
      return;
    }
    try {
      MailClientInfo mailClientInfo = new MailClientInfo();
      mailClientInfo.setClientName(clientName);
      mailClientInfo.setLastUpdateTime(new Date());
      restTemplate.postForLocation(updateclient, JacksonUtils.jsonObjectSerializer(mailClientInfo));
      logger.info("更新客户端信息成功");
    } catch (RestClientException e) {
      logger.error("调用接口更新客户端信息出错" + clientName + "--->" + updateclient, e);
      // TODO 邮件通知管理员,并且记录标记 一小时通知一次 直接发送就个邮件通知管理员
    }
  }

}
