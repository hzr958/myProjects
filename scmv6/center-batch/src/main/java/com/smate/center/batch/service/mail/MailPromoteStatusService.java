package com.smate.center.batch.service.mail;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.MailPromoteStatus;


public interface MailPromoteStatusService {

  MailPromoteStatus getMailStatusByTempCode(Integer tempCode) throws ServiceException;

  void saveMailPromoteStatus(MailPromoteStatus mailStatus) throws ServiceException;

}
