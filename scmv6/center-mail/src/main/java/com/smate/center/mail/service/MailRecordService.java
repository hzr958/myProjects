package com.smate.center.mail.service;

import java.util.List;

import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.model.MailRecord;

/**
 * 邮件发送记录服务
 * 
 * @author tsz
 *
 */
public interface MailRecordService {

  /**
   * 获取 待回收的 已分配发送账号 以及客户端记录
   * 
   * @param size
   * @param recoveryTime 回收时间 s (超过分配时间多少 s 就回收)
   * @return
   */
  public List<MailRecord> getToBeRecoveryMail(int size, int recoveryTime);

  /**
   * 按优先级规则 获取 待发送邮件
   * 
   * @param size
   * @return
   */
  public List<MailRecord> getToBeAllocatedMail(int size);

  /**
   * 保存更新mailrecord
   * 
   * @param mailRecord
   */

  public void updateMailRecord(MailRecord mailRecord);

  /**
   * 获取 待发送的数据
   * 
   * @param size
   * @return
   */
  public List<String> getToBeSendMail(int size, String client);

  /**
   * 更新邮件发送状态
   */
  public void updateMailRecordStatus(Long mailId, MailSendStatusEnum status, String msg);

}
