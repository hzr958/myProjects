package com.smate.center.batch.service.mail;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;


public interface DailyEmailLimitService {

  /**
   * 获取每日限制模板名称
   * 
   * @return
   * @throws ServiceException
   */
  List<String> getDailyEmailLimitTempName() throws ServiceException;

  /**
   * 保存每日发送日志
   * 
   * @param email 收件人邮箱
   * @param tempName 模版名
   * @throws ServiceException
   */
  void saveSendRecored(String email, String tempName) throws ServiceException;

  /**
   * 今天是否发送过邮件
   * 
   * @param email 收件人邮箱
   * @param tempName 模版名
   * @return
   * @throws ServiceException
   */
  boolean isSendToday(String email, String tempName) throws ServiceException;

}
