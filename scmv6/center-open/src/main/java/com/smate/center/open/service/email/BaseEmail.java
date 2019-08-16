package com.smate.center.open.service.email;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public abstract class BaseEmail {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  public void syncEmailInfo(Object... params) {
    try {
      Map<String, Object> mailMap = invoke(params);
      // 填写restful地址
      restTemplate.postForObject(sendRestfulUrl, mailMap, Object.class);
    } catch (Exception e) {
      logger.error("整理／同步邮件数据到邮件服务时出错", e);
    }
  }

  public abstract Map<String, Object> invoke(Object... params) throws Exception;
}

