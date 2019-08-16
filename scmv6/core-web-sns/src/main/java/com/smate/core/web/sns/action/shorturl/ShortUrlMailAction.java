package com.smate.core.web.sns.action.shorturl;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.web.sns.shorturl.service.ShortUrlMailService;

public class ShortUrlMailAction extends ActionSupport {
  private static final long serialVersionUID = 1L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private ShortUrlMailService shortUrlMailService;

  /**
   * 邮件链接短地址
   */
  @Action("/EL/*")
  public String EmailLink() {
    try {
      shortUrlMailService.doSendRedirect();
    } catch (Exception e) {
      logger.error("邮件链接 短地址 出错", e);
    }
    return null;
  }

}
