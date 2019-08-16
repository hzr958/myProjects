package com.smate.center.open.service.profile;

import java.util.List;

import com.smate.core.base.utils.model.security.Person;

public interface PsnInfoXmlService {

  String buildPsnListXmlStr(List<Person> personList) throws Exception;

  /**
   * 重构邮件显示效果.
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  String buildEmail(String email) throws Exception;

  /**
   * 随机生成由数字和字符组成的8位关联验证码.
   * 
   * @return
   * @throws ServiceException
   */
  String generateConnectedCode() throws Exception;
}
