package com.smate.web.management.model.mail;

/**
 * 邮件常量类
 * 
 * @author zzx
 *
 */
public class MailManageConstant {
  // sendStatus 发送状态 0=待分配 1=待发送 2=发送成功 3=黑名单 4=receiver不存在
  // 5邮件不在白名单 8=邮件发送出错9=邮件调度出错，10邮件正在发送
  // 11构造邮件发送信息出错
  public static final String to_be_distributed = "待分配";// 0
  public static final String to_be_sent = "待发送";// 1
  public static final String send_successfully = "发送成功";// 2
  public static final String blacklist = "黑名单";// 3
  public static final String receiver_inexistence = "收件箱不存在";// 4
  public static final String no_whitelist = "邮件不在白名单 ";// 5
  public static final String information_lock = "信息被锁定";// 7
  public static final String send_error = "邮件发送出错";// 8
  public static final String scheduling_error = "调度出错";// 9
  public static final String sending = "邮件正在发送";// 10
  public static final String build_send_error = "构造邮件发送信息出错";// 11
  // status 0=待构造邮件 1=构造成功 2=构造失败 3=用户不接收此类邮件 4=模版频率限制
  public static final String to_be_construct = "待构造邮件";// 0
  public static final String construct_successful = "构造成功";// 1
  public static final String construct_error = "构造失败";// 2
  public static final String receive_refuse = "用户不接收此类邮件";// 3
  public static final String frequency_limit = "模版频率限制";
}
