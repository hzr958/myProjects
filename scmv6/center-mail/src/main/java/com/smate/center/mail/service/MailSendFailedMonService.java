package com.smate.center.mail.service;

import java.util.List;

import javax.mail.Message;

import com.smate.center.mail.model.MailSender;

/**
 * 邮件发送失败监控接口
 * 
 * @author zzx
 *
 */
public interface MailSendFailedMonService {
  /**
   * 获取所有的发件邮箱对象
   * 
   * @return
   */
  List<MailSender> findSenderList();

  /**
   * 查找收件箱邮件列表
   * 
   * @param host
   * @param account
   * @param password
   * @return
   * @throws Exception
   */
  Message[] findInboxImap(String host, String account, String password) throws Exception;

  /**
   * 解析邮件信息
   * 
   * @param message
   * @param account
   * @throws Exception
   */
  void handleMessage(Message message, String account) throws Exception;

}
