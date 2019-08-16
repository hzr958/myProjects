package com.smate.center.batch.service.mail;

import java.util.Date;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.MailLog;
import com.smate.center.batch.model.mail.emailsrv.MailLogHis;
import com.smate.center.batch.model.mail.emailsrv.MailPromoteStatus;
import com.smate.core.base.utils.model.Page;


public interface MailLogService {

  void saveMailLog(MailLog mailLog) throws ServiceException;

  List<MailLog> getMailLogNotSend(Integer size, Long startMailId) throws ServiceException;

  void updateSuccessLog(Long mailId, Integer mailstatus) throws ServiceException;

  String getMailContextByMailId(Long mailId) throws ServiceException;

  Integer getNotSendPromoteMailCount(Integer tempCode) throws ServiceException;

  List<MailLog> getPromoteMailByTempAndStatus(Integer status, Integer tempCode, Long startMailId, Integer size)
      throws ServiceException;

  List<MailLog> getNotSendMailLogByTempCode(Integer tempCode) throws ServiceException;

  void saveMail(MailLog mailLog, String context) throws ServiceException;

  Page<String> getMailContextForPreView(Page<String> page, Integer tempId) throws ServiceException;

  List<MailLog> getPromoteMailByTempAndStatus2(Integer status, MailPromoteStatus mailStatus, Long startMailId,
      Integer size) throws ServiceException;

  Integer getNotSendPromoteMailCount2(MailPromoteStatus mailStatus) throws ServiceException;

  List<MailLog> getMailLogBeforeAssignMonths(Date assignMoths, Integer size) throws ServiceException;

  void backUpMailLog(List<MailLog> mailLogList) throws ServiceException;

  List<MailLogHis> getMailLogHisBeforeAssignMonths(Date assignMoths, Integer size) throws ServiceException;

  void clearMailLogHis(List<MailLogHis> mailLogHisList) throws ServiceException;

}
