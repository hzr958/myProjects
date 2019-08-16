package com.smate.core.base.email.service;

import com.smate.core.base.utils.exception.PublicException;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

public abstract class AbstractEmailSend implements EmailSendService {

  @Autowired
  private MailInitDataService mailInitDataService;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  public abstract Map<String, String> invoke(Object... params) throws PublicException;

  /**
   * 保存邮件信息
   * 
   * @param params
   * @throws PublicException
   */
  @Override
  public void syncEmailInfo(Object... params) throws PublicException {
    try {
      Map<String, String> paramData = invoke(params);
      if (MapUtils.isNotEmpty(paramData)) {
        restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
        // mailInitDataService.saveMailInitData(mailMap);
      }
    } catch (Exception e) {
      throw new PublicException("整理／同步邮件数据到邮件服务时出错", e);
    }
  }

}
