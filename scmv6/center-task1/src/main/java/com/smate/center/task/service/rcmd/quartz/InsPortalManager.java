package com.smate.center.task.service.rcmd.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.InsPortalRcmd;


public interface InsPortalManager {
  /**
   * 通过域名获取单位域名等信息.
   */
  InsPortalRcmd getInsPortalByInsId(Long insId) throws ServiceException;

}
