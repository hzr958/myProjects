package com.smate.center.task.service.email;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.email.ConstEmailInterval;


public interface EmailSendTaskDBService {

  ConstEmailInterval getConstEmailInterval(Integer etempCode) throws ServiceException;

  Boolean canExecuteEmailTask(Integer etempCode) throws ServiceException;

  void saveConstEmailInterval(ConstEmailInterval emailInterval) throws ServiceException;

  void updateNextDate(Integer etempCode) throws ServiceException;

}
