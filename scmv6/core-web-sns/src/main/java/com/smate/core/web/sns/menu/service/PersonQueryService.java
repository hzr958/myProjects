package com.smate.core.web.sns.menu.service;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.security.Person;

/**
 * 获取 人员信息类接口
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface PersonQueryService {
  /**
   * 取得个人基本信息.
   * 
   * @return Person
   * @throws SysServiceException
   */
  Person getPersonBaseInfo(Long psnId) throws SysServiceException;


}
