package com.smate.web.group.service.group.psn;

import java.util.Locale;

import com.smate.core.base.utils.model.security.Person;

public interface PersonManager {

  /**
   * @param personId
   * @return
   * @throws ServiceException
   */
  Person getPersonByRecommend(Long personId) throws Exception;

  /**
   * 获取人员姓名.
   * 
   * @param psnId
   * @param locale TODO
   * @return
   * @throws ServiceException
   */
  String getPsnName(Long psnId, Locale locale) throws Exception;

  String getPsnName(Person person, String locale) throws Exception;

  /**
   * 获取人员姓名.
   * 
   * @param person
   * @return
   * @throws ServiceException
   */
  String getPsnName(Person person) throws Exception;

}
