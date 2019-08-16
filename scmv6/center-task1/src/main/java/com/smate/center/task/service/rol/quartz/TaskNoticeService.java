package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.ClearTaskNoticeEvent;
import com.smate.center.task.model.rol.quartz.ClearTaskNoticeUserInfo;

public interface TaskNoticeService {

  void clearTaskNotice(ClearTaskNoticeEvent instance) throws ServiceException;

  void clearTaskNotice(ClearTaskNoticeEvent event, ClearTaskNoticeUserInfo userInfo) throws ServiceException;

}
