package com.smate.center.mail.connector.mailenum;

/**
 * 邮件发送状态 枚举
 * 
 * @author tsz
 *
 */
public enum MailSendStatusEnum {
  /*
   * 待分配
   */
  STATUS_0(0),
  /*
   * 待发送
   */
  STATUS_1(1),
  /*
   * 发送成功
   */
  STATUS_2(2),
  /*
   * 黑名单
   */
  STATUS_3(3),
  /*
   * receiver不存在
   */
  STATUS_4(4),
  /*
   * 邮件不在白名单
   */
  STATUS_5(5),
  /*
   * 
   */
  STATUS_6(6),
  /*
   * 邮件信息被锁定
   */
  STATUS_7(7),
  /*
   * 8邮件发送出错
   */
  STATUS_8(8),
  /*
   * 邮件调度出错
   */
  STATUS_9(9),
  /*
   * 邮件正在发送
   */
  STATUS_10(10),
  /*
   * 构造邮件发送信息出错
   */
  STATUS_11(11),
  /*
   * 邮件内容包含敏感词
   */
  STATUS_12(12),
  /*
   * 邮件模板不可用
   */
  STATUS_13(13);


  private int value;

  private MailSendStatusEnum(int value) {
    this.value = value;
  }

  public int toInt() {
    return this.value;
  }
}
