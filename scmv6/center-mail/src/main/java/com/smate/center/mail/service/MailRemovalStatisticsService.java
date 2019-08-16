package com.smate.center.mail.service;

import java.util.List;

import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.model.MailRecord;

/**
 * 邮件迁移统计接口
 * 
 * @author zzx
 *
 */
public interface MailRemovalStatisticsService {
  /**
   * 查找30内的原始邮件记录
   * 
   * @return
   * @throws Exception
   */
  List<MailOriginalData> findOriginalList(int size) throws Exception;

  /**
   * 查找30内的邮件发送记录
   * 
   * @return
   * @throws Exception
   */
  List<MailRecord> findRecordList(int size) throws Exception;

  /**
   * 处理原始数据到历史表
   * 
   * @param originalList
   * @throws Exception
   */
  void handleOriginalList(List<MailOriginalData> originalList) throws Exception;

  /**
   * 是否有需要处理的原始数据
   * 
   * @return
   * @throws Exception
   */
  boolean existOriginalList() throws Exception;

  /**
   * 处理邮件发送记录
   * 
   * @param mailRecordList
   * @throws Exception
   */
  void handleMailRecord(List<MailRecord> mailRecordList) throws Exception;

  /**
   * 是否有需要处理的邮件发送记录
   * 
   * @return
   * @throws Exception
   */
  boolean existRecordList() throws Exception;

  /**
   * 统计邮件信息
   * 
   * @throws Exception
   */
  void statisticsMailInfo() throws Exception;

}
