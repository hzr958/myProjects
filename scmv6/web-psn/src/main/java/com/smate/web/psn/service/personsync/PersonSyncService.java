package com.smate.web.psn.service.personsync;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psninfo.SyncPerson;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

public interface PersonSyncService {

  /**
   * 处理人员冗余信息
   * 
   * @param syncPerson
   * @throws ServiceException
   */
  void dealPersonSync(Person person, SyncPerson syncPerson) throws ServiceException;

  /**
   * 同步snsPerson数据
   * 
   * @throws ServiceException
   */
  void snsPersonSync(SnsPersonSyncMessage message) throws ServiceException;
}
