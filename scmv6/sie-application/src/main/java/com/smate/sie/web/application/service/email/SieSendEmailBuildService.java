package com.smate.sie.web.application.service.email;

import java.io.Serializable;
import java.util.Map;

/**
 * 发送邮件工具接口
 */
public interface SieSendEmailBuildService extends Serializable {

  /**
   * 描述,只会在表里出现一会
   */
  public static final String EMAIL_MSG = "msg";
  /**
   * 优先级，已废弃
   */
  @Deprecated
  public static final String EMAIL_PRIORLEVEL = "email_priorlevel";
  /**
   * 模板号
   */
  public static final String EMAIL_TEMPLATE_CODE = "email_template_code";
  /**
   * 接收邮箱
   */
  public static final String EMAIL_RECEIVE = "email_receive";
  /**
   * 接收人ID
   */
  public static final String EMAIL_RECEIVER_PSNID = "email_receiver_psnid";
  /**
   * 发送人ID
   */
  public static final String EMAIL_SENDER_PSNID = "email_sender_psnid";

  /**
   * 发送邮件
   * 
   * @param emailParam 邮件参数
   * @param mailData 邮件内容参数
   */
  Map<String, Object> sendEmail(Map<String, Object> emailParam, Map<String, Object> mailData);
}
