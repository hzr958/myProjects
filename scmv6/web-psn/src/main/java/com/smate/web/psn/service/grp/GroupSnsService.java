package com.smate.web.psn.service.grp;



import java.io.Serializable;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 群组资源接口.
 * 
 * @author zhuagnyanming
 * 
 */

public interface GroupSnsService extends Serializable {
  /**
   * 人员信息同步群组中.
   * 
   * @param message
   * @throws ServiceException
   */
  public void syncGroupMember(SnsPersonSyncMessage message) throws ServiceException;

}
