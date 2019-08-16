package com.smate.web.v8pub.service.sns.psnconfigpub;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.v8pub.exception.ServiceException;

public interface PersonService {

  /**
   * 获取人员的姓名信息
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Person getPersonName(Long psnId) throws ServiceException;

  String getName(Long psnId);
}
