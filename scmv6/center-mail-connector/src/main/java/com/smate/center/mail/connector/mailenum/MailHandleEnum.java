package com.smate.center.mail.connector.mailenum;

/**
 * 邮件处理枚举
 * 
 * @author zzx
 *
 */
public enum MailHandleEnum {
  /**
   * 邮件模版参数
   */
  MAIL_DATA("mailData"),
  /**
   * 邮件原始数据
   */
  MAIL_ORIGINAL_DATA("mailOriginalData"),
  /**
   * 发送人id,0=系统邮件
   */
  SENDER_PSN_ID("senderPsnId"),
  /**
   * 接收人id,0=非科研之友用户
   */
  RECEIVER_PSN_ID("receiverPsnId"),
  /**
   * 接收邮箱
   */
  RECEIVER("receiver"),
  /**
   * 邮件模版编码
   */
  MAIL_TEMPLATE_CODE("mailTemplateCode"),
  /**
   * 优先级
   */
  PRIOR_LEVEL("priorLevel"),
  /**
   * 描述
   */
  MSG("msg"),
  /**
   * 主题参数列表
   */
  SUBJECT_PARAM_LIST("subjectParamList"),
  /**
   * 列表对象参数
   */
  OBJ_LIST_MAP("objListMap"),
  /**
   * 取消订阅链接
   */
  UNSUBSCRIBE_URL("unsubscribeUrl"),
  /**
   * 域名
   */
  VIEW_MAIL_PATH("viewMailPath"),
  /**
   * 邮件详情链接
   */
  VIEW_MAIL_URL("viewMailUrl");



  private String value;

  private MailHandleEnum(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

}
